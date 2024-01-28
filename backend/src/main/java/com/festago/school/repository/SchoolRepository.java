package com.festago.school.repository;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByDomainOrName(String domain, String name);

    List<School> findAllByRegion(SchoolRegion schoolRegion);
}
