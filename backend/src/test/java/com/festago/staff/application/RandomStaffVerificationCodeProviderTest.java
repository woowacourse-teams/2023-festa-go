package com.festago.staff.application;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.staff.domain.StaffVerificationCode;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RandomStaffVerificationCodeProviderTest {

    RandomStaffVerificationCodeProvider codeProvider = new RandomStaffVerificationCodeProvider();

    @Test
    void 생성() {
        // given
        String abbreviation = "festa";
        School school = SchoolFixture.school().domain(abbreviation + ".ac.kr").build();
        Festival festival = FestivalFixture.festival().school(school).build();

        // when
        StaffVerificationCode code = codeProvider.provide(festival);

        // then
        assertSoftly(softly -> {
            softly.assertThat(code.getValue()).startsWith(abbreviation);
            softly.assertThat(code.getValue()).hasSize(abbreviation.length() + 4);
        });
    }
}
