package com.festago.admin.repository;

import com.festago.admin.domain.Admin;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryAdminRepository implements AdminRepository {

    private final ConcurrentHashMap<Long, Admin> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @Override
    public Admin save(Admin admin) {
        long id = autoIncrement.incrementAndGet();
        memory.put(id, new Admin(id, admin.getUsername(), admin.getPassword()));
        return memory.get(id);
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
}
