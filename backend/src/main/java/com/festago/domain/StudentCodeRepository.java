package com.festago.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCodeRepository extends JpaRepository<StudentCode, Long> {

    Optional<StudentCode> findByMember(Member member);
}
