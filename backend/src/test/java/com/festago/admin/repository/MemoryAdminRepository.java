package com.festago.admin.repository;

import com.festago.admin.domain.Admin;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryAdminRepository implements AdminRepository {

    private final ConcurrentHashMap<Long, Admin> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public Admin save(Admin admin) {
        Field idField = admin.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(admin, autoIncrement.incrementAndGet());
        memory.put(admin.getId(), admin);
        return admin;
    }

    @Override
    public Optional<Admin> findById(Long adminId) {
        return Optional.ofNullable(memory.get(adminId));
    }

    @Override
    public Optional<Admin> findByUsername(String username) {
        return memory.values().stream()
            .filter(admin -> Objects.equals(admin.getUsername(), username))
            .findAny();
    }

    @Override
    public boolean existsByUsername(String username) {
        return memory.values().stream()
            .anyMatch(admin -> Objects.equals(admin.getUsername(), username));
    }

    public void clear() {
        memory.clear();
    }
}
