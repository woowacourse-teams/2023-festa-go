package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.domain.Festival;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageTest {

    @Test
    void 공연이_축제_시작_일자_이전이면_예외() {
        // given
        LocalDateTime startTime = LocalDateTime.parse("2023-07-26T18:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-07-26T17:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(startTime.plusDays(1).toLocalDate())
            .endDate(startTime.plusDays(1).toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.stage()
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .festival(festival)
            .build()
        )
            .isInstanceOf(BadRequestException.class)
            .hasMessage("공연은 축제 기간 중에만 진행될 수 있습니다.");
    }

    @Test
    void 공연이_축제_종료_일자_이후이면_예외() {
        // given
        LocalDateTime startTime = LocalDateTime.parse("2023-07-26T18:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-07-26T17:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(startTime.minusDays(1).toLocalDate())
            .endDate(startTime.minusDays(1).toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.stage()
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .festival(festival)
            .build()
        )
            .isInstanceOf(BadRequestException.class)
            .hasMessage("공연은 축제 기간 중에만 진행될 수 있습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-07-26T18:00:00", "2023-07-26T18:00:01"})
    void 티켓_오픈_시간이_무대_시작시간과_같거나_이후이면_예외(LocalDateTime ticketOpenTime) {
        // given
        LocalDateTime startTime = LocalDateTime.parse("2023-07-26T18:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(startTime.toLocalDate())
            .endDate(startTime.toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.stage()
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .festival(festival)
            .build()
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("티켓 오픈 시간은 공연 시작 이전 이어야 합니다.");
    }

    @Test
    void 무대_생성() {
        // given
        LocalDateTime startTime = LocalDateTime.parse("2023-07-26T18:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-07-26T17:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(startTime.toLocalDate())
            .endDate(startTime.toLocalDate())
            .build();

        // when & then
        assertThatNoException().isThrownBy(() -> StageFixture.stage()
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .festival(festival)
            .build());
    }

}
