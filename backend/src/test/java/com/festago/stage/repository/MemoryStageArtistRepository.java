package com.festago.stage.repository;

import com.festago.stage.domain.StageArtist;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryStageArtistRepository implements StageArtistRepository {

    private final ConcurrentHashMap<Long, StageArtist> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public StageArtist save(StageArtist stageArtist) {
        Field idField = stageArtist.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(stageArtist, autoIncrement.incrementAndGet());
        memory.put(stageArtist.getId(), stageArtist);
        return stageArtist;
    }

    @Override
    public List<StageArtist> findAllByStageId(Long stageId) {
        return memory.values().stream()
            .filter(stageArtist -> Objects.equals(stageArtist.getStageId(), stageId))
            .toList();
    }
}
