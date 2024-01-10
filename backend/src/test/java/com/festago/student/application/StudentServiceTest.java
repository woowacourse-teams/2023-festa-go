package com.festago.student.application;

import static com.festago.common.exception.ErrorCode.ALREADY_STUDENT_VERIFIED;
import static com.festago.common.exception.ErrorCode.DUPLICATE_STUDENT_EMAIL;
import static com.festago.common.exception.ErrorCode.INVALID_STUDENT_VERIFICATION_CODE;
import static com.festago.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.student.domain.Student;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import com.festago.student.dto.StudentResponse;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.student.infrastructure.MockMailClient;
import com.festago.student.infrastructure.RandomVerificationCodeProvider;
import com.festago.student.repository.MemoryStudentCodeRepository;
import com.festago.student.repository.MemoryStudentRepository;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StudentCodeFixture;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StudentServiceTest {

    MailClient mailClient = new MockMailClient();

    VerificationCodeProvider codeProvider = new RandomVerificationCodeProvider();

    MemoryStudentCodeRepository studentCodeRepository = new MemoryStudentCodeRepository();

    MemorySchoolRepository schoolRepository = new MemorySchoolRepository();

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    MemoryStudentRepository studentRepository = new MemoryStudentRepository();

    Clock clock = spy(Clock.systemDefaultZone());

    StudentService studentService = new StudentService(
        mailClient,
        codeProvider,
        studentCodeRepository,
        schoolRepository,
        memberRepository,
        studentRepository,
        clock
    );

    @BeforeEach
    void setUp() {
        studentCodeRepository.clear();
        schoolRepository.clear();
        memberRepository.clear();
        studentRepository.clear();
        reset(clock);
    }

    @Nested
    class sendVerificationMail {

        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();
        StudentSendMailRequest request;

        @BeforeEach
        void setUp() {
            memberRepository.save(member);
            schoolRepository.save(school);
            request = new StudentSendMailRequest("ash", school.getId());
        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_멤버이면_예외() {
                // given
                Long unsavedMemberId = member.getId() + 1;

                // when & then
                assertThatThrownBy(() -> studentService.sendVerificationMail(unsavedMemberId, request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(MEMBER_NOT_FOUND.getMessage());
            }

            @Test
            void 존재하지_않는_학교면_예외() {
                // given
                Long memberId = member.getId();
                var invalidRequest = new StudentSendMailRequest("ash", school.getId() + 1);

                // when & then
                assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, invalidRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(SCHOOL_NOT_FOUND.getMessage());
            }

            @Test
            void 이미_존재하는_학생이면_예외() {
                // given
                Long memberId = member.getId();
                studentRepository.save(new Student(member, school, "glen"));

                // when & then
                assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(ALREADY_STUDENT_VERIFIED.getMessage());
            }

            @Test
            void 이미_존재하는_이메일이면_예외() {
                // given
                Long memberId = member.getId();
                Member otherMember = memberRepository.save(MemberFixture.member().build());
                studentRepository.save(new Student(otherMember, school, "ash"));

                // when & then
                assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(DUPLICATE_STUDENT_EMAIL.getMessage());
            }
        }

        @Nested
        class 성공 {

            @Test
            void StudentCode가_있으면_갱신된다() {
                // given
                LocalDateTime now = LocalDateTime.parse("2023-01-10T16:00:00");
                doReturn(Instant.parse("2023-01-10T17:00:00Z"))
                    .when(clock)
                    .instant();
                StudentCode studentCode = StudentCodeFixture.studentCode()
                    .member(member)
                    .school(school)
                    .issuedAt(now)
                    .build();
                studentCodeRepository.save(studentCode);

                // when
                studentService.sendVerificationMail(1L, request);

                // then
                assertThat(studentCode.getIssuedAt()).isAfter(now);
            }

            @Test
            void StudentCode가_없으면_저장된다() {
                // when
                studentService.sendVerificationMail(1L, request);

                // then
                assertThat(studentCodeRepository.findByMemberId(member.getId())).isPresent();
            }
        }

    }

    @Nested
    class 학생_인증 {

        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();
        StudentVerificateRequest request = new StudentVerificateRequest("123456");

        @BeforeEach
        void setUp() {
            memberRepository.save(member);
            schoolRepository.save(school);
        }

        @Test
        void 이미_학생인증정보가_존재하면_예외() {
            // given
            Long memberId = member.getId();
            studentRepository.save(new Student(member, school, "glen"));

            // when & then
            assertThatThrownBy(() -> studentService.verify(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ALREADY_STUDENT_VERIFIED.getMessage());
        }

        @Test
        void 인증_코드가_존재하지_않으면_예외() {
            // given
            Long memberId = member.getId();

            // when & then
            assertThatThrownBy(() -> studentService.verify(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_STUDENT_VERIFICATION_CODE.getMessage());
        }

        @Test
        void 성공하면_학생이_저장되고_StudentCode가_삭제된다() {
            // given
            Long memberId = member.getId();
            studentCodeRepository.save(
                new StudentCode(new VerificationCode(request.code()), school, member, "glen",
                    LocalDateTime.now(clock)));

            // when
            studentService.verify(memberId, request);

            // when & then
            assertSoftly(softly -> {
                softly.assertThat(studentRepository.existsByMemberId(memberId)).isTrue();
                softly.assertThat(studentCodeRepository.findByMemberId(memberId)).isEmpty();
            });
        }
    }

    @Nested
    class 멤버_아이디로_인증정보_조회 {

        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();

        @BeforeEach
        void setUp() {
            memberRepository.save(member);
            schoolRepository.save(school);
        }

        @Test
        void 학생_인증된_사용자의_경우_verified가_true이고_학교의_식별자가_조회된다() {
            // given
            Long memberId = member.getId();
            studentRepository.save(new Student(member, school, "glen"));

            // when
            StudentResponse actual = studentService.findVerification(memberId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.isVerified()).isTrue();
                softly.assertThat(actual.school().id()).isEqualTo(school.getId());
            });
        }

        @Test
        void 학생_인증되지_않은_사용자의_경우_verified가_false이고_학교가_null이다() {
            // given
            Long memberId = 1L;

            // when
            StudentResponse actual = studentService.findVerification(memberId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.isVerified()).isFalse();
                softly.assertThat(actual.school()).isNull();
            });
        }
    }
}
