package com.festago.ticket.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import com.festago.stage.domain.Stage;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.StageTicketResponse;
import com.festago.ticket.dto.StageTicketsResponse;
import com.festago.ticket.repository.MemoryTicketAmountRepository;
import com.festago.ticket.repository.MemoryTicketRepository;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceTest {

    MemoryTicketAmountRepository ticketAmountRepository = new MemoryTicketAmountRepository();

    MemoryTicketRepository ticketRepository = new MemoryTicketRepository(ticketAmountRepository);

    MemoryStageRepository stageRepository = new MemoryStageRepository();

    Clock clock = spy(Clock.systemDefaultZone());

    TicketService ticketService = new TicketService(
        ticketRepository,
        stageRepository,
        clock
    );

    @BeforeEach
    void setUp() {
        ticketAmountRepository.clear();
        ticketRepository.count();
        stageRepository.clear();
        reset(clock);
    }

    @Test
    void 공연_아이디로_모든_티켓의_정보_조회() {
        // given
        Stage stage = StageFixture.stage().build();
        stageRepository.save(stage);

        Ticket studentTicket = TicketFixture.ticket().ticketType(TicketType.STUDENT).stage(stage).build();
        ticketRepository.save(studentTicket);
        Ticket visitorTicket = TicketFixture.ticket().ticketType(TicketType.VISITOR).stage(stage).build();
        ticketRepository.save(visitorTicket);

        // when
        StageTicketsResponse actual = ticketService.findStageTickets(stage.getId());

        // then
        assertThat(actual.tickets())
            .map(StageTicketResponse::id)
            .contains(studentTicket.getId(), visitorTicket.getId())
            .hasSize(2);
    }
}
