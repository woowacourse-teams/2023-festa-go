package com.festago.school.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SchoolRepository extends Repository<School, Long> {

    default School getOrThrow(Long schoolId) {
        return findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    School save(School school);

    Optional<School> findById(Long id);

    void deleteById(Long id);

    boolean existsByDomain(String domain);

    boolean existsByName(String name);

    Optional<School> findByName(String name);

    boolean existsById(Long id);
}
