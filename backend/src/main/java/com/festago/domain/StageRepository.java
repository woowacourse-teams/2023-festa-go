package com.festago.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StageRepository extends JpaRepository<Stage, Long> {

    List<Stage> findAllByFestival(Festival festival);

    @Query("""
        SELECT s FROM Stage s
        JOIN FETCH s.tickets t
        JOIN FETCH t.ticketAmount
        WHERE s.festival.id = :festivalId
        """)
    List<Stage> findAllDetailByFestivalId(@Param("festivalId") Long festivalId);
}
