package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.StageTicketResponse;
import com.festago.dto.StageTicketsResponse;
import com.festago.support.TicketFixture;
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

    @Mock
    MemberTicketRepository memberTicketRepository;

    @InjectMocks
    TicketService ticketService;

    @Test
    void 공연_아이디로_모든_티켓의_정보_조회() {
        // given
        List<Ticket> tickets = List.of(
            TicketFixture.ticket().ticketType(TicketType.STUDENT).totalAmount(100).build(),
            TicketFixture.ticket().ticketType(TicketType.STUDENT).totalAmount(200).build(),
            TicketFixture.ticket().ticketType(TicketType.VISITOR).totalAmount(300).build(),
            TicketFixture.ticket().ticketType(TicketType.VISITOR).totalAmount(400).build()
        );
        given(ticketRepository.findAllByStageId(anyLong()))
            .willReturn(tickets);
        given(memberTicketRepository.countByTicketTypeAndStageId(eq(TicketType.STUDENT), anyLong()))
            .willReturn(100);
        given(memberTicketRepository.countByTicketTypeAndStageId(eq(TicketType.VISITOR), anyLong()))
            .willReturn(200);

        // when
        StageTicketsResponse actual = ticketService.findStageTickets(1L);

        // then
        assertThat(actual.tickets()).containsOnly(
            new StageTicketResponse(TicketType.STUDENT, 300, 200),
            new StageTicketResponse(TicketType.VISITOR, 700, 500)
        );
    }
}
