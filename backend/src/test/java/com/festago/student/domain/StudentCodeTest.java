package com.festago.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.TooManyRequestException;
import com.festago.common.exception.UnexpectedException;
import com.festago.common.exception.ValidException;
import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StudentCodeTest {

    @Test
    void 성공() {
        // given
        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();
        LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
        VerificationCode verificationCode = new VerificationCode("123456");

        // when
        StudentCode studentCode = new StudentCode(1L, verificationCode, school, member, "glen", issuedAt);

        // then
        assertThat(studentCode.getId()).isEqualTo(1L);
    }

    @Test
    void username의_길이가_255자를_초과하면_예외() {
        // given
        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();
        LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
        VerificationCode verificationCode = new VerificationCode("123456");
        String username = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new StudentCode(1L, verificationCode, school, member, username, issuedAt))
            .isInstanceOf(ValidException.class)
            .hasMessageContaining("username");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void username이_null_또는_공백이면_예외(String username) {
        // given
        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().build();
        LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
        VerificationCode verificationCode = new VerificationCode("123456");

        // when & then
        assertThatThrownBy(() -> new StudentCode(1L, verificationCode, school, member, username, issuedAt))
            .isInstanceOf(ValidException.class)
            .hasMessageContaining("username");
    }

    @Test
    void 이메일_반환_성공() {
        // given
        Member member = MemberFixture.member().build();
        School school = SchoolFixture.school().domain("fiddich.com").build();
        LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
        VerificationCode verificationCode = new VerificationCode("123456");

        // when
        StudentCode studentCode = new StudentCode(1L, verificationCode, school, member, "glen", issuedAt);

        // then
        assertThat(studentCode.getEmail()).isEqualTo("glen@fiddich.com");
    }

    @Nested
    class 재발급 {

        @Test
        void 재발급할_코드에_식별자가_있으면_예외() {
            // given
            Member member = MemberFixture.member().build();
            School school = SchoolFixture.school().build();
            LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
            VerificationCode verificationCode = new VerificationCode("123456");
            StudentCode studentCode = new StudentCode(1L, verificationCode, school, member, "ash", issuedAt);

            LocalDateTime future = LocalDateTime.MAX;
            StudentCode newStudentCode = new StudentCode(2L, verificationCode, school, member, "glen", future);

            // when & then
            assertThatThrownBy(() -> studentCode.reissue(newStudentCode))
                .isInstanceOf(UnexpectedException.class)
                .hasMessage("새로 발급할 인증 코드는 식별자가 없어야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 30})
        void 발급한지_30초_이내면_예외(long second) {
            // given
            Member member = MemberFixture.member().build();
            School school = SchoolFixture.school().build();
            LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
            VerificationCode verificationCode = new VerificationCode("123456");
            StudentCode studentCode = new StudentCode(1L, verificationCode, school, member, "ash", issuedAt);

            LocalDateTime future = issuedAt.plusSeconds(second);
            StudentCode newStudentCode = new StudentCode(null, verificationCode, school, member, "glen", future);

            // when & then
            assertThatThrownBy(() -> studentCode.reissue(newStudentCode))
                .isInstanceOf(TooManyRequestException.class)
                .hasMessage(ErrorCode.TOO_FREQUENT_REQUESTS.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {31, 999, 9999})
        void 발급한지_30초_이후면_성공(long second) {
            // given
            Member member = MemberFixture.member().build();
            School school = SchoolFixture.school().build();
            LocalDateTime issuedAt = LocalDateTime.parse("2023-11-19T02:00:00");
            VerificationCode verificationCode = new VerificationCode("123456");
            StudentCode studentCode = new StudentCode(1L, verificationCode, school, member, "ash", issuedAt);

            LocalDateTime future = issuedAt.plusSeconds(second);
            StudentCode newStudentCode = new StudentCode(null, verificationCode, school, member, "glen", future);

            // when
            studentCode.reissue(newStudentCode);

            // then
            assertThat(studentCode.getUsername()).isEqualTo("glen");
        }
    }
}
