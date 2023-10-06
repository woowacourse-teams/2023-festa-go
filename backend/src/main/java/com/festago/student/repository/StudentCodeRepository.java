package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCodeRepository extends JpaRepository<StudentCode, Long> {

    void deleteByMember(Member member);

    Optional<StudentCode> findByCodeAndMember(VerificationCode code, Member member);

    Optional<StudentCode> findByMemberId(Long memberId);
}
