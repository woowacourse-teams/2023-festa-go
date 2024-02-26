package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryFestivalRepository implements FestivalRepository {

    private final ConcurrentHashMap<Long, Festival> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public Festival save(Festival festival) {
        Field idField = festival.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(festival, autoIncrement.incrementAndGet());
        memory.put(festival.getId(), festival);
        return festival;
    }

    @Override
    public boolean existsBySchoolId(Long schoolId) {
        return memory.values().stream()
            .anyMatch(festival -> Objects.equals(festival.getSchool().getId(), schoolId));
    }

    @Override
    public Optional<Festival> findById(Long festivalId) {
        return Optional.ofNullable(memory.get(festivalId));
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
    public List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime) {
        throw new UnsupportedOperationException();
    }
}
