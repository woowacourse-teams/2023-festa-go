package com.festago.school.repository;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemorySchoolRepository implements SchoolRepository {

    private final ConcurrentHashMap<Long, School> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @Override
    @SneakyThrows
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
    public void deleteById(Long id) {
        memory.remove(id);
    }

    @Override
    public List<School> findAllByRegion(SchoolRegion schoolRegion) {
        return memory.values().stream()
            .filter(school -> school.getRegion() == schoolRegion)
            .toList();
    }

    @Override
    public boolean existsByDomain(String domain) {
        return memory.values().stream()
            .anyMatch(school -> Objects.equals(school.getDomain(), domain));
    }

    @Override
    public boolean existsByName(String name) {
        return memory.values().stream()
            .anyMatch(school -> Objects.equals(school.getName(), name));
    }

    @Override
    public Optional<School> findByName(String name) {
        return memory.values().stream()
            .filter(school -> Objects.equals(school.getName(), name))
            .findAny();
    }
}
