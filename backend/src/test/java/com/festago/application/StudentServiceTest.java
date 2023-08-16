package com.festago.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

import com.festago.domain.MailClient;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.School;
import com.festago.domain.SchoolRepository;
import com.festago.domain.StudentCodeRepository;
import com.festago.domain.StudentRepository;
import com.festago.domain.VerificationCode;
import com.festago.domain.VerificationCodeProvider;
import com.festago.dto.StudentSendMailRequest;
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
}
