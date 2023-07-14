package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketTest {

    @Test
    void 입장시간_전_입장_불가() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.minusMinutes(10);
        Ticket ticket = new Ticket(entryTime);

        // when && then
        assertThat(ticket.canEntry(time)).isFalse();
    }

    @Test
    void 입장시간_하루_지나면_입장_불가() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.plusDays(1);
        Ticket ticket = new Ticket(entryTime);

        // when && then
        assertThat(ticket.canEntry(time)).isFalse();
    }

    @Test
    void 입장_가능() {
        // given
        LocalDateTime entryTime = LocalDateTime.now();
        LocalDateTime time = entryTime.plusMinutes(10);
        Ticket ticket = new Ticket(entryTime);

        // when && then
        assertThat(ticket.canEntry(time)).isTrue();
    }
}
