package com.festago.stage.repository;

import com.festago.stage.domain.StageQueryInfo;
import org.springframework.data.repository.Repository;

public interface StageQueryInfoRepository extends Repository<StageQueryInfo, Long> {

    StageQueryInfo save(StageQueryInfo stageQueryInfo);
}
