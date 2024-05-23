package com.festago.admin.repository;

import com.festago.admin.domain.Admin;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;
import java.util.Optional;

public class MemoryAdminRepository extends AbstractMemoryRepository<Admin> implements AdminRepository {

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
