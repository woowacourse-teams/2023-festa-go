package com.festago.festival.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;
import java.util.Optional;

public class MemoryFestivalQueryInfoRepository extends AbstractMemoryRepository<FestivalQueryInfo> implements FestivalInfoRepository {

    @Override
    public Optional<FestivalQueryInfo> findByFestivalId(Long festivalId) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getFestivalId(), festivalId))
            .findAny();
    }

    @Override
    public void deleteByFestivalId(Long festivalId) {
        memory.entrySet().removeIf(it -> Objects.equals(it.getValue().getFestivalId(), festivalId));
    }
}
