package com.festago.application;

import com.festago.domain.MemberTicketRepository;
import com.festago.domain.TicketRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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

//    @Test
//    void 공연_아이디로_모든_티켓의_정보_조회() {
//        // given
//        List<Ticket> tickets = List.of(
//            TicketFixture.ticket().ticketType(TicketType.STUDENT).build(),
//            TicketFixture.ticket().ticketType(TicketType.STUDENT).build(),
//            TicketFixture.ticket().ticketType(TicketType.VISITOR).build(),
//            TicketFixture.ticket().ticketType(TicketType.VISITOR).build()
//        );
//        StageTicketsResponse actual = ticketService.findStageTickets(1L);
//
//        // then
//        assertThat(actual.tickets()).containsOnly(
//            new StageTicketResponse(TicketType.STUDENT, 300, 200),
//            new StageTicketResponse(TicketType.VISITOR, 700, 500)
//        );
//    }
}
