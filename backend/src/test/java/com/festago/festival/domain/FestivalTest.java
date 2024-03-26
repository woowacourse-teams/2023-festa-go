package com.festago.festival.domain;

import static com.festago.common.exception.ErrorCode.INVALID_FESTIVAL_DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.school.domain.School;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalTest {

    @Test
    void 시작일자가_종료일자_이전이면_예외() {
        // given
        LocalDate today = LocalDate.now();
        School school = SchoolFixture.builder().build();
        LocalDate tomorrow = today.plusDays(1);

        // when & then
        assertThatThrownBy(() -> new Festival("테코대학교", tomorrow, today, school))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(INVALID_FESTIVAL_DURATION.getMessage());
    }

    @Nested
    class 축제_기간_중이_아닌지_검사 {

        @Test
        void 축제_시작_일자_이전이면_참() {
            // given
            LocalDateTime time = LocalDateTime.now();
            Festival festival = FestivalFixture.builder()
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
            Festival festival = FestivalFixture.builder()
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
            Festival festival = FestivalFixture.builder()
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
