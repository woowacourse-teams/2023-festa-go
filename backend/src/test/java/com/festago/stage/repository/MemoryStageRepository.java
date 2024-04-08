package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import com.festago.support.AbstractMemoryRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MemoryStageRepository extends AbstractMemoryRepository<Stage> implements StageRepository {


    @Override
    public void flush() {
        //NOOP
    }

    @Override
    public boolean existsByFestivalId(Long festivalId) {
        return memory.values().stream()
            .anyMatch(stage -> Objects.equals(stage.getFestival().getId(), festivalId));
    }

    @Override
    public List<Stage> findAllByFestivalId(Long festivalId) {
        return memory.values().stream()
            .filter(stage -> Objects.equals(stage.getFestival().getId(), festivalId))
            .toList();
    }

    @Override
    public List<Stage> findAllDetailByFestivalId(Long festivalId) {
        return findAllByFestivalId(festivalId);
    }

    @Override
    public Optional<Stage> findByIdWithFetch(Long id) {
        return findById(id);
    }
}
