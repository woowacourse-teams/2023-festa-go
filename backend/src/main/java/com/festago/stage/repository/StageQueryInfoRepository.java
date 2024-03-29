package com.festago.stage.repository;

import com.festago.stage.domain.StageQueryInfo;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StageQueryInfoRepository extends Repository<StageQueryInfo, Long> {

    StageQueryInfo save(StageQueryInfo stageQueryInfo);

    Optional<StageQueryInfo> findByStageId(Long stageId);

    void deleteByStageId(Long stageId);
}
