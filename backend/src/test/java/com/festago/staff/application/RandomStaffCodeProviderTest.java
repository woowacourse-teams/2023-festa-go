package com.festago.staff.application;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RandomStaffCodeProviderTest {

    RandomStaffCodeProvider codeProvider = new RandomStaffCodeProvider();

    @Test
    void 생성() {
        // given
        String abbreviation = "festa";
        School school = SchoolFixture.school().domain(abbreviation + ".ac.kr").build();
        Festival festival = FestivalFixture.festival().school(school).build();

        // when
        String code = codeProvider.provide(festival);

        // then
        assertSoftly(softly -> {
            softly.assertThat(code).startsWith(abbreviation);
            softly.assertThat(code).hasSize(abbreviation.length() + 4);
        });
    }
}
