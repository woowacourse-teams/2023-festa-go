package com.festago.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByUsernameAndSchoolId(String username, Long id);

    boolean existsByMemberId(Long id);
}
