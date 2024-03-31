package com.festago.mock.repository;

import com.festago.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockSchoolRepository extends JpaRepository<School, Long> {

}
