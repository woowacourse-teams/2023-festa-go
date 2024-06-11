package com.festago.ticket.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.ticket.domain.FakeTicket;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.repository.MemoryNewTicketRepository;
import com.festago.ticket.repository.NewTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketMaxReserveCountChangeServiceTest {

    TicketMaxReserveCountChangeService ticketMaxReserveCountChangeService;

    NewTicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository = new MemoryNewTicketRepository();
        ticketMaxReserveCountChangeService = new TicketMaxReserveCountChangeService(
            ticketRepository
        );
    }

    @Nested
    class changeMaxReserveAmount {

        @Test
        void 티켓이_존재하지_않으면_예외() {
            // when & then
            assertThatThrownBy(() -> ticketMaxReserveCountChangeService.changeMaxReserveAmount(4885L, 100))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 티켓이_존재하면_최대_예매_가능_개수가_변경된다() {
            // given
            NewTicket ticket = ticketRepository.save(new FakeTicket(1L, 100));

            // when
            ticketMaxReserveCountChangeService.changeMaxReserveAmount(ticket.getId(), 4885);

            // then
            assertThat(ticketRepository.findById(ticket.getId()))
                .map(NewTicket::getMaxReserveAmount)
                .hasValue(4885);
        }
    }
}
