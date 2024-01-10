package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StudentCodeRepository extends Repository<StudentCode, Long> {

    StudentCode save(StudentCode studentCode);

    void deleteByMember(Member member);

    Optional<StudentCode> findByCodeAndMember(VerificationCode code, Member member);

    Optional<StudentCode> findByMemberId(Long memberId);
}
