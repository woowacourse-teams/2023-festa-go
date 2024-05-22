package com.festago.stage.domain;

import static com.festago.common.exception.ErrorCode.INVALID_STAGE_START_TIME;
import static com.festago.common.exception.ErrorCode.INVALID_TICKET_OPEN_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.domain.Festival;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
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
        Festival festival = FestivalFixture.builder()
            .startDate(startTime.plusDays(1).toLocalDate())
            .endDate(startTime.plusDays(1).toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.builder()
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
        Festival festival = FestivalFixture.builder()
            .startDate(startTime.minusDays(1).toLocalDate())
            .endDate(startTime.minusDays(1).toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.builder()
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
        Festival festival = FestivalFixture.builder()
            .startDate(startTime.toLocalDate())
            .endDate(startTime.toLocalDate())
            .build();

        // when & then
        assertThatThrownBy(() -> StageFixture.builder()
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
        Festival festival = FestivalFixture.builder()
            .startDate(startTime.toLocalDate())
            .endDate(startTime.toLocalDate())
            .build();

        // when & then
        assertThatNoException().isThrownBy(() -> StageFixture.builder()
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .festival(festival)
            .build());
    }

    @Nested
    class renewArtists {

        Stage stage;

        @BeforeEach
        void setUp() {
            stage = StageFixture.builder().id(1L).build();
        }

        @Test
        void 아티스트를_추가할_수_있다() {
            // when
            stage.renewArtists(List.of(1L, 2L, 3L));

            // then
            assertThat(stage.getArtistIds())
                .containsExactly(1L, 2L, 3L);
        }

        @Test
        void 추가하려는_아티스트가_기존_아티스트에_없으면_기존_아티스트는_삭제된다() {
            // given
            stage.renewArtists(List.of(1L));

            // when
            stage.renewArtists(List.of(2L, 3L));

            // then
            assertThat(stage.getArtistIds())
                .doesNotContain(1L)
                .containsExactly(2L, 3L);
        }
    }
}
