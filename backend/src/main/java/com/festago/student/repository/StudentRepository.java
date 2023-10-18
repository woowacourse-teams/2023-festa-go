package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByMemberAndSchoolId(Member member, Long schoolId);

    boolean existsByUsernameAndSchoolId(String username, Long id);

    boolean existsByMemberId(Long id);

    @Query("""
        SELECT st
        FROM Student st
        INNER JOIN FETCH st.school sc
        WHERE st.member.id = :memberId
        """)
    Optional<Student> findByMemberIdWithFetch(@Param("memberId") Long memberId);
}
