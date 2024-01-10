package com.festago.school.repository;

import com.festago.school.domain.School;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemorySchoolRepository implements SchoolRepository {

    private final Map<Long, School> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public School save(School school) {
        Field idField = school.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(school, autoIncrement.incrementAndGet());
        memory.put(school.getId(), school);
        return school;
    }

    @Override
    public Optional<School> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public boolean existsByDomainOrName(String domain, String name) {
        return memory.values().stream()
            .anyMatch(school -> school.getDomain().equals(domain) || school.getName().equals(name));
    }

    @Override
    public List<School> findAll() {
        return memory.values().stream().toList();
    }

    @Override
    public void deleteById(Long schoolId) {
        memory.remove(schoolId);
    }

    @Override
    public void flush() {
        // NOOP
    }

    public void clear() {
        memory.clear();
    }
}
