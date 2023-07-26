package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.FestivalFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalTest {

    @Nested
    class 축제_기간_중이_아닌지_검사 {

        @Test
        void 축제_시작_일자_이전이면_참() {
            // given
            LocalDateTime time = LocalDateTime.now();
            Festival festival = FestivalFixture.festival()
                .startDate(time.plusDays(1).toLocalDate())
                .endDate(time.plusDays(4).toLocalDate())
                .build();

            // when
            boolean actual = festival.isNotInDuration(time);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 축제_종료_일자_이후이면_참() {
            // given
            LocalDateTime time = LocalDateTime.now();
            Festival festival = FestivalFixture.festival()
                .startDate(time.minusDays(4).toLocalDate())
                .endDate(time.minusDays(1).toLocalDate())
                .build();

            // when
            boolean actual = festival.isNotInDuration(time);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 축제_기간중이면_거짓() {
            // given
            LocalDateTime time = LocalDateTime.now();
            Festival festival = FestivalFixture.festival()
                .startDate(time.minusDays(1).toLocalDate())
                .endDate(time.toLocalDate())
                .build();

            // when
            boolean actual = festival.isNotInDuration(time);

            // then
            assertThat(actual).isFalse();
        }
    }
}
