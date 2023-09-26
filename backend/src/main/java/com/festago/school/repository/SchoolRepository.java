package com.festago.school.repository;

import com.festago.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsByDomainOrName(String domain, String name);

}
