package com.festago.school.repository;

import com.festago.school.domain.School;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemorySchoolRepository implements SchoolRepository {

    private final Map<Long, School> db = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @SneakyThrows
    @Override
    public School save(School school) {
        Field idField = school.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(school, id.getAndIncrement());
        db.put(school.getId(), school);
        return school;
    }

    @Override
    public Optional<School> findById(Long schoolId) {
        return Optional.ofNullable(db.get(schoolId));
    }

    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return db.get(id) != null;
    }

    @Override
    public boolean existsByDomain(String domain) {
        return db.values().stream()
            .anyMatch(it -> it.getDomain().equals(domain));
    }

    @Override
    public boolean existsByName(String name) {
        return db.values().stream()
            .anyMatch(it -> it.getName().equals(name));
    }

    @Override
    public Optional<School> findByName(String name) {
        return db.values().stream()
            .filter(it -> it.getName().equals(name))
            .findFirst();
    }
}
