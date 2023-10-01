package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
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
        School school = SchoolFixture.school().build();

        // when & then
        assertThatThrownBy(() -> new Festival("테코대학교", today.plusDays(1), today, school))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("축제 시작 일자는 종료일자 이전이어야합니다.");
    }

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
