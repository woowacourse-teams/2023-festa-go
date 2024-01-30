package com.festago.school.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    default School getOrThrow(Long schoolId) {
        return findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    /**
     * @deprecated API 버저닝이 적용되면 해당 메서드 삭제
     */
    @Deprecated(forRemoval = true)
    boolean existsByDomainOrName(String domain, String name);

    List<School> findAllByRegion(SchoolRegion schoolRegion);

    boolean existsByDomain(String domain);

    boolean existsByName(String name);

    Optional<School> findByName(String name);
}
