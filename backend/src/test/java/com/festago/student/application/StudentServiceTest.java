package com.festago.student.application;

import static com.festago.common.exception.ErrorCode.ALREADY_STUDENT_VERIFIED;
import static com.festago.common.exception.ErrorCode.DUPLICATE_STUDENT_EMAIL;
import static com.festago.common.exception.ErrorCode.INVALID_STUDENT_VERIFICATION_CODE;
import static com.festago.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.domain.Student;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import com.festago.student.dto.StudentResponse;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.student.infrastructure.MockMailClient;
import com.festago.student.infrastructure.RandomVerificationCodeProvider;
import com.festago.student.repository.StudentCodeRepository;
import com.festago.student.repository.StudentRepository;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.SetUpMockito;
import com.festago.support.StudentFixture;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StudentServiceTest {

    @Spy
    MailClient mailClient = new MockMailClient();

    @Spy
    VerificationCodeProvider codeProvider = new RandomVerificationCodeProvider();

    @Mock
    StudentCodeRepository studentCodeRepository;

    @Mock
    SchoolRepository schoolRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    StudentRepository studentRepository;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    StudentService studentService;

    @Nested
    class 학생_인증_메일_전송_요청 {

        StudentSendMailRequest request;

        @BeforeEach
        void setUp() {
            request = new StudentSendMailRequest("ash", 1L);
            Member member = MemberFixture.member().id(1L).build();
            School school = SchoolFixture.school().id(1L).build();

            SetUpMockito
                .given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
            SetUpMockito
                .given(schoolRepository.findById(anyLong()))
                .willReturn(Optional.of(school));
            SetUpMockito
                .given(studentCodeRepository.findByMemberId(anyLong()))
                .willReturn(Optional.empty());
            SetUpMockito
                .given(studentRepository.existsByMemberId(anyLong()))
                .willReturn(false);
            SetUpMockito
                .given(studentRepository.existsByUsernameAndSchoolId(anyString(), anyLong()))
                .willReturn(false);
        }

        @Test
        void 존재하지_않는_멤버이면_예외() {
            // given
            given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 존재하지_않는_학교면_예외() {
            // given
            given(schoolRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 이미_존재하는_학생이면_예외() {
            // given
            given(studentRepository.existsByMemberId(anyLong()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ALREADY_STUDENT_VERIFIED.getMessage());
        }

        @Test
        void 이미_존재하는_이메일이면_예외() {
            // given
            given(studentRepository.existsByUsernameAndSchoolId(anyString(), anyLong()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_STUDENT_EMAIL.getMessage());
        }

        @Test
        void 인증메일_전송() {
            assertThatNoException()
                .isThrownBy(() -> studentService.sendVerificationMail(1L, request));
        }
    }

    @Nested
    class 학생_인증 {

        @Test
        void 이미_학생인증정보가_존재하면_예외() {
            // given
            Long memberId = 1L;
            StudentVerificateRequest request = new StudentVerificateRequest("123456");
            given(studentRepository.existsByMemberId(memberId))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.verify(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ALREADY_STUDENT_VERIFIED.getMessage());
        }

        @Test
        void 인증_코드가_존재하지_않으면_예외() {
            // given
            Long memberId = 1L;
            StudentVerificateRequest request = new StudentVerificateRequest("123456");
            given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(MemberFixture.member().build()));
            given(studentCodeRepository.findByCodeAndMember(any(), any()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.verify(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_STUDENT_VERIFICATION_CODE.getMessage());
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            StudentVerificateRequest request = new StudentVerificateRequest("123456");
            Member member = MemberFixture.member().build();
            given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
            given(studentCodeRepository.findByCodeAndMember(any(), any()))
                .willReturn(Optional.of(new StudentCode(
                    new VerificationCode("123456"),
                    new School("snu.ac.kr", "서울대학교"),
                    member,
                    "ohs",
                    LocalDateTime.now()
                )));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> studentService.verify(memberId, request));
        }
    }

    @Nested
    class 멤버_아이디로_인증정보_조회 {

        @Test
        void 학생_인증된_멤버의_경우() {
            // given
            Long memberId = 1L;
            School school = SchoolFixture.school().id(2L).build();
            Student student = StudentFixture.student().id(3L).school(school).build();
            given(studentRepository.findByMemberIdWithFetch(memberId))
                .willReturn(Optional.of(student));

            // when
            StudentResponse actual = studentService.findVerification(memberId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.isVerified()).isTrue();
                softly.assertThat(actual.school().id()).isEqualTo(school.getId());
            });
        }

        @Test
        void 학생_인증되지_않은_사용자의_경우() {
            // given
            Long memberId = 1L;
            given(studentRepository.findByMemberIdWithFetch(memberId))
                .willReturn(Optional.empty());

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
