package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Optional;
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

    @Query("""
        SELECT s FROM Stage s
        LEFT JOIN s.festival f
        LEFT JOIN f.school sc
        WHERE s.id = :id
        """)
    Optional<Stage> findByIdWithFetch(@Param("id") Long id);
}
