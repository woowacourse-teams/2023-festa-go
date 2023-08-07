package com.festago.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StageRepository extends JpaRepository<Stage, Long> {

    @Query("""
        SELECT s FROM Stage s
        LEFT JOIN FETCH s.tickets t
        LEFT JOIN FETCH t.ticketAmount
        WHERE s.festival.id = :festivalId
        """)
    List<Stage> findAllDetailByFestivalId(@Param("festivalId") Long festivalId);
}
