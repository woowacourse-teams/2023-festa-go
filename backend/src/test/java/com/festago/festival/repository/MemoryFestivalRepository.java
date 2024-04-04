package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;

public class MemoryFestivalRepository extends AbstractMemoryRepository<Festival> implements FestivalRepository {

    @Override
    public boolean existsBySchoolId(Long schoolId) {
        return memory.values().stream()
            .anyMatch(festival -> Objects.equals(festival.getSchool().getId(), schoolId));
    }

    @Override
    public void deleteById(Long festivalId) {
        memory.remove(festivalId);
    }

    @Override
    public boolean existsById(Long festivalId) {
        return memory.containsKey(festivalId);
    }
}
