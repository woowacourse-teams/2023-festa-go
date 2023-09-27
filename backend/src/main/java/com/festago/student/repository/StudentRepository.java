package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByMemberAndSchoolId(Member member, Long schoolId);

    boolean existsByUsernameAndSchoolId(String username, Long id);

    boolean existsByMemberId(Long id);
}
