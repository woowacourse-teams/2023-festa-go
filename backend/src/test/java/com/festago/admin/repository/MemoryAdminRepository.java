package com.festago.admin.repository;

import com.festago.admin.domain.Admin;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryAdminRepository implements AdminRepository {

    private final Map<Long, Admin> memory = new HashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong(1);

    @Override
    public Admin save(Admin admin) {
        if (admin.getId() != null) {
            memory.put(admin.getId(), admin);
            return admin;
        }
        while (true) {
            long id = autoIncrement.get();
            if (!memory.containsKey(id)) {
                memory.put(id, admin);
                return admin;
            }
            autoIncrement.incrementAndGet();
        }
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
