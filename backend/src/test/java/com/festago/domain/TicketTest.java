package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.exception.BadRequestException;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketTest {

    @Test
    void 입장시간_전_입장_불가() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.minusMinutes(10);

        Ticket ticket = TicketFixture.ticket()
            .entryTime(entryTime)
            .build();

        // when && then
        assertThat(ticket.canEntry(time)).isFalse();
    }

    @Test
    void 입장시간_하루_지나면_입장_불가() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.plusDays(1);

        Ticket ticket = TicketFixture.ticket()
            .entryTime(entryTime)
            .build();

        // when && then
        assertThat(ticket.canEntry(time)).isFalse();
    }

    @Test
    void 입장_가능() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.plusMinutes(10);

        Ticket ticket = TicketFixture.ticket()
            .entryTime(entryTime)
            .build();

        // when && then
        assertThat(ticket.canEntry(time)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 총_수량이_1_미만이면_예외(int totalAmount) {
        // given
        LocalDateTime now = LocalDateTime.now();
        Stage stage = StageFixture.stage()
            .startTime(now.plusHours(2))
            .build();

        // when & then
        assertThatThrownBy(() -> new Ticket(stage, TicketType.VISITOR, totalAmount, now))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("티켓은 적어도 한장 이상 발급해야합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-07-26T18:00:00", "2023-07-26T18:00:01"})
    void 입장_시간이_축제_이후면_예외(LocalDateTime entryTime) {
        // given
        LocalDateTime stageStartTime = LocalDateTime.parse("2023-07-26T18:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.plusDays(4).toLocalDate())
            .build();

        Stage stage = StageFixture.stage()
            .festival(festival)
            .startTime(stageStartTime)
            .build();

        // when & then
        assertThatThrownBy(() -> new Ticket(stage, TicketType.VISITOR, 100, entryTime))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
    }

    @Test
    void 입장_시간이_공연_시작_12시간_이전이면_예외() {
        // given
        LocalDateTime entryTime = LocalDateTime.parse("2023-07-26T05:59:59");
        LocalDateTime stageStartTime = LocalDateTime.parse("2023-07-26T18:00:00");
        Festival festival = FestivalFixture.festival()
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.plusDays(4).toLocalDate())
            .build();

        Stage stage = StageFixture.stage()
            .festival(festival)
            .startTime(stageStartTime)
            .build();

        // when & then
        assertThatThrownBy(() -> new Ticket(stage, TicketType.VISITOR, 100, entryTime))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
    }
}
