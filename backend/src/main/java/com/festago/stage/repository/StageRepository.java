package com.festago.stage.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StageRepository extends Repository<Stage, Long> {

    default Stage getOrThrow(Long stageId) {
        return findById(stageId).orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }

    Stage save(Stage stage);

    Optional<Stage> findById(Long stageId);

    void deleteById(Long stageId);

    boolean existsByFestivalId(Long festivalId);

    List<Stage> findAllByFestivalId(Long festivalId);

    @Query("""
        select s
        from Stage s
        join fetch s.festival
        left join fetch s.artists
        where s.id = :id
        """)
    Optional<Stage> findByIdWithFetch(@Param("id") Long id);
}
