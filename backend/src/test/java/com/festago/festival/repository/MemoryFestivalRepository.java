package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryFestivalRepository implements FestivalRepository {

    private final Map<Long, Festival> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public Festival save(Festival festival) {
        Field idField = festival.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(festival, autoIncrement.incrementAndGet());
        memory.put(festival.getId(), festival);
        return festival;
    }

    @Override
    public Optional<Festival> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public void deleteById(Long festivalId) {
        memory.remove(festivalId);
    }

    @Override
    public void flush() {
        // NOOP
    }

    @Override
    public List<Festival> findByFilter(FestivalFilter festivalFilter, LocalDate currentTime) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        memory.clear();
    }
}
