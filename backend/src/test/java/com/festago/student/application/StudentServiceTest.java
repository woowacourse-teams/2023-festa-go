package com.festago.student.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.domain.StudentCode;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.infrastructure.MockMailClient;
import com.festago.student.infrastructure.RandomVerificationCodeProvider;
import com.festago.student.repository.StudentCodeRepository;
import com.festago.student.repository.StudentRepository;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StudentCodeFixture;
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

            lenient()
                .when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
            lenient()
                .when(schoolRepository.findById(anyLong()))
                .thenReturn(Optional.of(school));
            lenient()
                .when(studentCodeRepository.findByMemberId(anyLong()))
                .thenReturn(Optional.empty());
            lenient()
                .when(studentRepository.existsByMemberId(anyLong()))
                .thenReturn(false);
            lenient()
                .when(studentRepository.existsByUsernameAndSchoolId(anyString(), anyLong()))
                .thenReturn(false);
        }

        @Test
        void 존재하지_않는_멤버이면_예외() {
            // given
            given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 멤버입니다.");
        }

        @Test
        void 존재하지_않는_학교면_예외() {
            // given
            given(schoolRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 학교입니다.");
        }

        @Test
        void 너무_잦은_요청이면_예외() {
            // given
            StudentSendMailRequest request = new StudentSendMailRequest("ash", 1L);
            LocalDateTime currentTime = LocalDateTime.now(clock);
            LocalDateTime issuedAt = currentTime.minusSeconds(30);
            StudentCode studentCode = StudentCodeFixture.studentCode().issuedAt(issuedAt).build();
            given(studentCodeRepository.findByMemberId(anyLong()))
                .willReturn(Optional.of(studentCode));

            // when
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("너무 잦은 요청입니다. 잠시 후 다시 시도해주세요.");
        }

        @Test
        void 이미_존재하는_학생이면_예외() {
            // given
            given(studentRepository.existsByMemberId(anyLong()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 학교 인증이 완료된 사용자입니다.");
        }

        @Test
        void 이미_존재하는_이메일이면_예외() {
            // given
            given(studentRepository.existsByUsernameAndSchoolId(anyString(), anyLong()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 인증된 이메일입니다.");
        }

        @Test
        void 인증메일_전송() {
            assertThatNoException()
                .isThrownBy(() -> studentService.sendVerificationMail(1L, request));
        }
    }
}
