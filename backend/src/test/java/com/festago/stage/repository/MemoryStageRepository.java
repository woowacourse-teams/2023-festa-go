package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryStageRepository implements StageRepository {

    private final ConcurrentHashMap<Long, Stage> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public Stage save(Stage stage) {
        Field idField = stage.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(stage, autoIncrement.incrementAndGet());
        memory.put(stage.getId(), stage);
        return stage;
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
