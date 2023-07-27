package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 입장시간_24시간이_지나면_입장_불가() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.plusHours(24);

        Ticket ticket = TicketFixture.ticket()
            .entryTime(entryTime)
            .build();

        // when && then
        assertThat(ticket.canEntry(time)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-07-28T17:59:59", "2023-07-27T18:00:00"})
    void 입장_가능(LocalDateTime time) {
        // given
        LocalDateTime entryTime = LocalDateTime.parse("2023-07-27T18:00:00");

        Ticket ticket = TicketFixture.ticket()
            .entryTime(entryTime)
            .build();

        // when && then
        assertThat(ticket.canEntry(time)).isTrue();
    }
}
