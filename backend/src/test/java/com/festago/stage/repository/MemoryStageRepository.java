package com.festago.stage.repository;

import com.festago.stage.domain.Stage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryStageRepository implements StageRepository {

    private final Map<Long, Stage> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public Stage save(Stage stage) {
        Field idField = stage.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(stage, autoIncrement.incrementAndGet());
        memory.put(stage.getId(), stage);
        return stage;
    }

    @Override
    public Optional<Stage> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public void deleteById(Long stageId) {
        memory.remove(stageId);
    }

    @Override
    public void flush() {
        // NOOP
    }

    @Override
    public List<Stage> findAllDetailByFestivalId(Long festivalId) {
        return memory.values().stream()
            .filter(stage -> stage.getFestival().getId().equals(festivalId))
            .toList();
    }

    @Override
    public Optional<Stage> findByIdWithFetch(Long id) {
        return findById(id);
    }

    public void clear() {
        memory.clear();
    }
}
