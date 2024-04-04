package com.festago.school.repository;

import com.festago.school.domain.School;
import com.festago.support.AbstractMemoryRepository;
import java.util.Optional;

public class MemorySchoolRepository extends AbstractMemoryRepository<School> implements SchoolRepository {

    @Override
    public void deleteById(Long id) {
        memory.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return memory.get(id) != null;
    }

    @Override
    public boolean existsByDomain(String domain) {
        return memory.values().stream()
            .anyMatch(it -> it.getDomain().equals(domain));
    }

    @Override
    public boolean existsByName(String name) {
        return memory.values().stream()
            .anyMatch(it -> it.getName().equals(name));
    }

    @Override
    public Optional<School> findByName(String name) {
        return memory.values().stream()
            .filter(it -> it.getName().equals(name))
            .findFirst();
    }
}
