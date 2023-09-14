package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.exception.BadRequestException;
import com.festago.ticket.domain.TicketAmount;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketAmountTest {

    @Test
    void 티켓의_최대_수량과_현재_예매한_수량과_같으면_매진() {
        // given
        TicketAmount ticketAmount = new TicketAmount();
        ticketAmount.addTotalAmount(2);

        // when
        ticketAmount.increaseReservedAmount();
        ticketAmount.increaseReservedAmount();

        // then
        assertThatThrownBy(ticketAmount::increaseReservedAmount)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("매진된 티켓입니다.");
    }
}
