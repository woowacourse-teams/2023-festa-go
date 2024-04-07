package com.festago.school.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.application.SchoolDeleteService;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.domain.Student;
import com.festago.student.repository.StudentRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StudentFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 * 서비스 통합 테스트 시 필요한 의존이 너무 많은 것 같다. 차라리 Cucumber를 사용한 통합 테스트로 해당 테스트가 하는 역할을 옮기고, 서비스 통합 테스트는 Stub을 사용한 단위 테스트로 변경하여
 * 서비스가 행하고자 하는 비즈니스 로직을 나타내는 테스트로 하는게 어떨까 deleteSchool() 메서드의 행위는 Validator.validate()를 호출하여, 던져진 예외가 있으면 삭제에 실패한다. 라고
 * 볼 수 있다.
 */
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchoolDeleteServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolDeleteService schoolDeleteService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Nested
    class deleteSchool {

        School school;

        @BeforeEach
        void setUp() {
            school = schoolRepository.save(SchoolFixture.builder().build());
        }

        @Test
        void 학교에_등록된_축제가_있으면_삭제에_실패한다() {
            // given
            Long schoolId = school.getId();
            Festival festival = FestivalFixture.builder().school(school).build();
            festivalRepository.save(festival);

            // when & then
            assertThatThrownBy(() -> schoolDeleteService.deleteSchool(schoolId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.SCHOOL_DELETE_CONSTRAINT_EXISTS_FESTIVAL.getMessage());
        }

        @Test
        void 학교에_등록된_학생이_있으면_삭제에_실패한다() {
            // given
            Long schoolId = school.getId();
            Member member = memberRepository.save(MemberFixture.builder().build());
            Student student = StudentFixture.builder().member(member).school(school).build();
            studentRepository.save(student);

            // when & then
            assertThatThrownBy(() -> schoolDeleteService.deleteSchool(schoolId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.SCHOOL_DELETE_CONSTRAINT_EXISTS_STUDENT.getMessage());
        }

        @Test
        void Validator의_검증이_정상이면_학교가_삭제된다() {
            // given
            Long schoolId = school.getId();

            // when
            schoolDeleteService.deleteSchool(schoolId);

            // then
            assertThat(schoolRepository.findById(1L)).isEmpty();
        }
    }
}
