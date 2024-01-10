package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.Student;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StudentRepository extends Repository<Student, Long>, StudentRepositoryCustom {

    boolean existsByMemberAndSchoolId(Member member, Long schoolId);

    boolean existsByUsernameAndSchoolId(String username, Long schoolId);

    boolean existsByMemberId(Long memberId);

    Student save(Student student);

    Optional<Student> findById(Long id);
}
