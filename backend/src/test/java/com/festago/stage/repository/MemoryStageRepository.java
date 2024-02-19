package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryStageRepository implements StageRepository {

    private final ConcurrentHashMap<Long, Stage> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    public Stage save(Stage stage) {
        long id = autoIncrement.incrementAndGet();
        memory.put(id,
            new Stage(id, stage.getStartTime(), stage.getLineUp(), stage.getTicketOpenTime(), stage.getFestival()));
        return memory.get(id);
    }

    @Override
    public Optional<Stage> findById(Long stageId) {
        return Optional.ofNullable(memory.get(stageId));
    }

    @Override
    public void deleteById(Long stageId) {
        memory.remove(stageId);
    }

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
