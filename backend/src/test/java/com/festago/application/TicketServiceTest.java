package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.stage.domain.Stage;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.application.TicketService;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.StageTicketResponse;
import com.festago.ticket.dto.StageTicketsResponse;
import com.festago.ticket.repository.TicketRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketService ticketService;

    @Test
    void 공연_아이디로_모든_티켓의_정보_조회() {
        // given
        long stageId = 1L;
        Stage stage = StageFixture.stage().id(stageId).build();
        List<Ticket> tickets = List.of(
            TicketFixture.ticket().id(1L).ticketType(TicketType.STUDENT).stage(stage).build(),
            TicketFixture.ticket().id(2L).ticketType(TicketType.VISITOR).stage(stage).build()
        );
        given(ticketRepository.findAllByStageIdWithFetch(stageId))
            .willReturn(tickets);

        // when
        StageTicketsResponse actual = ticketService.findStageTickets(stageId);

        // then
        List<Long> ticketIds = actual.tickets().stream()
            .map(StageTicketResponse::id)
            .toList();
        assertThat(ticketIds).containsExactlyInAnyOrder(1L, 2L);
    }
}
