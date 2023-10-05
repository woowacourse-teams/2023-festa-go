package com.festago.domain;

import static com.festago.common.exception.ErrorCode.INVALID_STAGE_START_TIME;
import static com.festago.common.exception.ErrorCode.INVALID_TICKET_OPEN_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
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
            .hasMessage(INVALID_STAGE_START_TIME.getMessage());
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
            .hasMessage(INVALID_STAGE_START_TIME.getMessage());
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
            .isInstanceOf(BadRequestException.class)
            .hasMessage(INVALID_TICKET_OPEN_TIME.getMessage());
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

    @Nested
    class 해당_축제의_무대인지_확인 {

        Long festivalId;
        Stage stage;

        @BeforeEach
        void setUp() {
            festivalId = 1L;
            Festival festival = FestivalFixture.festival().id(festivalId).build();
            stage = StageFixture.stage().festival(festival).build();
        }

        @Test
        void 참() {
            // when
            boolean actual = stage.belongsToFestival(festivalId);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 거짓() {
            // when
            boolean actual = stage.belongsToFestival(festivalId + 1L);

            // then
            assertThat(actual).isFalse();
        }
    }
}
