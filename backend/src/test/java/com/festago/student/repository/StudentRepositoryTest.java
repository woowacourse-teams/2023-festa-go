package com.festago.student.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.domain.Student;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StudentFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 멤버id로_조회() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        School school = schoolRepository.save(SchoolFixture.school().build());
        Student student = studentRepository.save(StudentFixture.student().school(school).member(member).build());

        // when
        Optional<Student> actual = studentRepository.findByMemberIdWithFetch(member.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isNotEmpty();
            softly.assertThat(actual.get().getId()).isEqualTo(student.getId());
        });
    }
}
