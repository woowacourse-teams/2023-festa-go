package com.festago.festival.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryFestivalQueryInfoRepository implements FestivalInfoRepository {

    private final ConcurrentHashMap<Long, FestivalQueryInfo> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public FestivalQueryInfo save(FestivalQueryInfo festivalQueryInfo) {
        Field idField = festivalQueryInfo.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(festivalQueryInfo, autoIncrement.incrementAndGet());
        memory.put(festivalQueryInfo.getId(), festivalQueryInfo);
        return festivalQueryInfo;
    }

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

    @Override
    public List<FestivalQueryInfo> findAll() {
        return new ArrayList<>(memory.values());
    }
}
