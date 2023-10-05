package com.festago.domain;

import static com.festago.common.exception.ErrorCode.INVALID_MIN_TICKET_AMOUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.ticket.domain.TicketEntryTime;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketEntryTimeTest {

    @Test
    void 총_수량이_음수일_경우_에외() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new TicketEntryTime(now, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("TicketEntryTime 의 필드로 허용된 범위를 넘은 column 을 넣을 수 없습니다.");
    }

    @Test
    void 총_수량이_1_미만이면_예외() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new TicketEntryTime(now, 0))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(INVALID_MIN_TICKET_AMOUNT.getMessage());
    }
}
