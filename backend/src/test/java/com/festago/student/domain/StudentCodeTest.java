package com.festago.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StudentCodeTest {

    @Nested
    class 재발급_가능한지_확인 {

        @Test
        void 발급한지_30초_이내면_거짓() {
            // given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime issuedAt = currentTime.minusSeconds(30);
            School school = SchoolFixture.school().build();
            Member member = MemberFixture.member().build();
            StudentCode studentCode = new StudentCode(1L, new VerificationCode("123456"),
                school, member, "ash", issuedAt);

            // when
            boolean actual = studentCode.canReissue(currentTime);

            // then
            assertThat(actual).isEqualTo(false);
        }

        @Test
        void 발급한지_30초_이후면_참() {
            // given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime issuedAt = currentTime.minusSeconds(31);
            School school = SchoolFixture.school().build();
            Member member = MemberFixture.member().build();
            StudentCode studentCode = new StudentCode(1L, new VerificationCode("123456"),
                school, member, "ash", issuedAt);

            // when
            boolean actual = studentCode.canReissue(currentTime);

            // then
            assertThat(actual).isEqualTo(true);
        }
    }

    @Test
    void 학생인증코드_재발급() {
        // given
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime issuedAt = currentTime.minusSeconds(31);

        VerificationCode oldCode = new VerificationCode("111111");
        VerificationCode newCode = new VerificationCode("222222");

        School oldSchool = SchoolFixture.school().build();
        School newSchool = SchoolFixture.school().build();

        String oldUsername = "ash";
        String newUsername = "pooh";

        Member member = MemberFixture.member().build();

        StudentCode studentCode = new StudentCode(1L, oldCode, oldSchool, member, oldUsername, issuedAt);

        // when
        studentCode.reissue(newCode, newSchool, newUsername);

        // then
        assertSoftly(softly -> {
            softly.assertThat(studentCode.getCode()).isEqualTo(newCode);
            softly.assertThat(studentCode.getSchool()).isEqualTo(newSchool);
            softly.assertThat(studentCode.getUsername()).isEqualTo(newUsername);
            softly.assertThat(studentCode.getMember()).isEqualTo(member);
        });
    }
}
