package com.festago.stage.repository;

import static java.util.stream.Collectors.toUnmodifiableSet;

import com.festago.stage.domain.StageArtist;
import com.festago.support.AbstractMemoryRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MemoryStageArtistRepository extends AbstractMemoryRepository<StageArtist> implements StageArtistRepository {

    @Override
    public Set<Long> findAllArtistIdByStageId(Long stageId) {
        return memory.values().stream()
            .filter(stageArtist -> Objects.equals(stageArtist.getStageId(), stageId))
            .map(StageArtist::getArtistId)
            .collect(toUnmodifiableSet());
    }

    @Override
    public Set<Long> findAllArtistIdByStageIdIn(List<Long> stageIds) {
        return memory.values().stream()
            .filter(stageArtist -> stageIds.contains(stageArtist.getStageId()))
            .map(StageArtist::getArtistId)
            .collect(toUnmodifiableSet());
    }

    @Override
    public void deleteByStageId(Long stageId) {
        memory.entrySet().removeIf(entry -> Objects.equals(entry.getValue().getStageId(), stageId));
    }
}
