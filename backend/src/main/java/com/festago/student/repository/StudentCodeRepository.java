package com.festago.student.repository;

import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import com.festago.zmember.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCodeRepository extends JpaRepository<StudentCode, Long> {

    void deleteByMember(Member member);

    Optional<StudentCode> findByCodeAndMember(VerificationCode code, Member member);
}
