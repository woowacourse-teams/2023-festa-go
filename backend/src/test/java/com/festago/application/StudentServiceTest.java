package com.festago.application;

import static com.festago.common.exception.ErrorCode.ALREADY_STUDENT_VERIFIED;
import static com.festago.common.exception.ErrorCode.DUPLICATE_STUDENT_EMAIL;
import static com.festago.common.exception.ErrorCode.INVALID_STUDENT_VERIFICATION_CODE;
import static com.festago.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.application.MailClient;
import com.festago.student.application.StudentService;
import com.festago.student.application.VerificationCodeProvider;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.student.repository.StudentCodeRepository;
import com.festago.student.repository.StudentRepository;
import com.festago.support.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    MailClient mailClient;

    @Mock
    VerificationCodeProvider codeProvider;

    @Mock
    StudentCodeRepository studentCodeRepository;

    @Mock
    SchoolRepository schoolRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    StudentRepository studentRepository;

    @Nested
    class 인증_메일_전송 {

        @Test
        void 이미_학생인증정보가_존재하면_예외() {
            // given
            Long memberId = 1L;
            Long schoolId = 1L;
            String username = "user";
            StudentSendMailRequest request = new StudentSendMailRequest(username, schoolId);
            given(studentRepository.existsByMemberId(memberId))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ALREADY_STUDENT_VERIFIED.getMessage());
        }

        @Test
        void 중복되는_이메일이면_예외() {
            // given
            Long memberId = 1L;
            Long schoolId = 1L;
            String username = "user";
            StudentSendMailRequest request = new StudentSendMailRequest(username, schoolId);

            given(studentRepository.existsByMemberId(memberId))
                .willReturn(false);
            given(studentRepository.existsByUsernameAndSchoolId(username, schoolId))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_STUDENT_EMAIL.getMessage());
        }

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;
            Long schoolId = 1L;
            String username = "user";
            StudentSendMailRequest request = new StudentSendMailRequest(username, schoolId);

            given(studentRepository.existsByMemberId(memberId))
                .willReturn(false);
            given(studentRepository.existsByUsernameAndSchoolId(username, schoolId))
                .willReturn(false);
            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 학교가_없으면_예외() {
            // given
            Long memberId = 1L;
            Long schoolId = 1L;
            String username = "user";
            Member member = MemberFixture.member()
                .id(memberId)
                .build();
            StudentSendMailRequest request = new StudentSendMailRequest(username, schoolId);

            given(studentRepository.existsByMemberId(memberId))
                .willReturn(false);
            given(studentRepository.existsByUsernameAndSchoolId(username, schoolId))
                .willReturn(false);
            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
            given(schoolRepository.findById(schoolId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> studentService.sendVerificationMail(memberId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(SCHOOL_NOT_FOUND.getMessage());
        }


        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            Long schoolId = 1L;
            String username = "user";
            Member member = MemberFixture.member()
                .id(memberId)
                .build();
            StudentSendMailRequest request = new StudentSendMailRequest(username, schoolId);

            given(studentRepository.existsByMemberId(memberId))
                .willReturn(false);
            given(studentRepository.existsByUsernameAndSchoolId(username, schoolId))
                .willReturn(false);
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
            given(schoolRepository.findById(schoolId))
                .willReturn(Optional.of(new School(schoolId, "festago.ac.kr", "페스타고대학교")));
            given(codeProvider.provide())
                .willReturn(new VerificationCode("123456"));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> studentService.sendVerificationMail(memberId, request));
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
            assertThatThrownBy(() -> studentService.verificate(memberId, request))
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
            assertThatThrownBy(() -> studentService.verificate(memberId, request))
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
                    "ohs"
                )));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> studentService.verificate(memberId, request));
        }
    }
}
