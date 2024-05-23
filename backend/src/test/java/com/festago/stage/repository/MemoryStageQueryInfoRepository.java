package com.festago.stage.repository;

import com.festago.stage.domain.StageQueryInfo;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;
import java.util.Optional;

public class MemoryStageQueryInfoRepository extends AbstractMemoryRepository<StageQueryInfo> implements
    StageQueryInfoRepository {

    @Override
    public Optional<StageQueryInfo> findByStageId(Long stageId) {
        return memory.values()
            .stream()
            .filter(stageQueryInfo -> Objects.equals(stageQueryInfo.getStageId(), stageId))
            .findAny();
    }

    @Override
    public void deleteByStageId(Long stageId) {
        memory.entrySet()
            .removeIf(entry -> Objects.equals(entry.getValue().getStageId(), stageId));
    }
}
