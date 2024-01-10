package com.festago.stage.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StageRepository extends Repository<Stage, Long>, StageRepositoryCustom {

    default Stage getOrThrow(Long stageId) {
        return findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }

    Stage save(Stage stage);

    Optional<Stage> findById(Long id);

    void deleteById(Long stageId);

    void flush();
}
