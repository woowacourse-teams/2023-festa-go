package com.festago.school.repository;

import com.festago.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByDomain(String domain);

    boolean existsByName(String name);
}
