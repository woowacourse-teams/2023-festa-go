package com.festago.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.support.TicketFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketTest {

    @Test
    void 입장시간을_추가한다() {
        // given
        Ticket ticket = TicketFixture
            .ticket()
            .build();

        LocalDateTime startTime = ticket.getStage().getStartTime();

        // when
        ticket.addTicketEntryTime(startTime.minusHours(3), 100);
        ticket.addTicketEntryTime(startTime.minusHours(2), 200);

        // then
        assertSoftly(softly -> {
            softly.assertThat(ticket.getTicketAmount().get().getTotalAmount()).isEqualTo(300);
            softly.assertThat(ticket.getTicketEntryTimes()).hasSize(2);
        });
    }
}
