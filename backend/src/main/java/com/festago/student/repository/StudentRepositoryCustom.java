package com.festago.student.repository;

import com.festago.student.domain.Student;
import java.util.Optional;

public interface StudentRepositoryCustom {

    Optional<Student> findByMemberIdWithFetch(Long memberId);
}
