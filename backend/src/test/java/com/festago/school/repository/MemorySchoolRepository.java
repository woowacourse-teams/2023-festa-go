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
        return existsByField("domain", domain);
    }

    @Override
    public boolean existsByName(String name) {
        return existsByField("name", name);
    }

    @Override
    public Optional<School> findByName(String name) {
        return findByField("name", name);
    }

    @SneakyThrows
    private Optional<School> findByField(String fieldName, Object value) {
        for (School school : db.values()) {
            if (hasFieldValue(school, fieldName, value)) {
                return Optional.of(school);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    private boolean existsByField(String fieldName, Object value) {
        for (School school : db.values()) {
            if (hasFieldValue(school, fieldName, value)) {
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    private boolean hasFieldValue(School school, String fieldName, Object fieldValue) {
        Field field = school.getClass()
            .getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(school).equals(fieldValue);
    }
}
