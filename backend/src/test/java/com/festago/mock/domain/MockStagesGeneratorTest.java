package com.festago.mock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import com.festago.support.fixture.FestivalFixture;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockStagesGeneratorTest {

    MockStagesGenerator mockStagesGenerator = new MockStagesGenerator();

    @Test
    void 축제의_모든_기간에_공연을_생성한다() {
        // given
        LocalDate now = LocalDate.parse("2077-06-30");
        Festival festival = FestivalFixture.builder()
            .startDate(now)
            .endDate(now.plusDays(2))
            .build();

        // when
        List<Stage> actual = mockStagesGenerator.generate(festival);

        // then
        LocalDate festivalStartDate = festival.getStartDate();
        LocalDate festivalEndDate = festival.getEndDate();
        long festivalPeriod = festivalStartDate.until(festivalEndDate, ChronoUnit.DAYS) + 1L;
        assertThat(actual)
            .size()
            .isEqualTo(festivalPeriod);
    }
}
