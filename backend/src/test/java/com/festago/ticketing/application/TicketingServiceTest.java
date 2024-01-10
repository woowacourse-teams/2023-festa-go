package com.festago.ticketing.application;

import static com.festago.common.exception.ErrorCode.NEED_STUDENT_VERIFICATION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import com.festago.common.exception.BadRequestException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.student.repository.MemoryStudentRepository;
import com.festago.support.MemberFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.MemoryTicketAmountRepository;
import com.festago.ticket.repository.MemoryTicketRepository;
import com.festago.ticketing.dto.TicketingRequest;
import com.festago.ticketing.repository.MemoryMemberTicketRepository;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketingServiceTest {

    MemoryMemberTicketRepository memberTicketRepository = new MemoryMemberTicketRepository();

    MemoryTicketAmountRepository ticketAmountRepository = new MemoryTicketAmountRepository();

    MemoryTicketRepository ticketRepository = new MemoryTicketRepository(ticketAmountRepository);

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    MemoryStudentRepository studentRepository = new MemoryStudentRepository();

    Clock clock = spy(Clock.systemDefaultZone());

    TicketingService ticketingService = new TicketingService(
        memberTicketRepository,
        ticketAmountRepository,
        ticketRepository,
        memberRepository,
        studentRepository,
        clock
    );

    @BeforeEach
    void setUp() {
        memberTicketRepository.clear();
        ticketAmountRepository.clear();
        ticketRepository.clear();
        memberRepository.clear();
        studentRepository.clear();
        reset(clock);
    }

    @Test
    void 재학생용_티켓인데_학생인증이_되지_않았으면_예외() {
        // given
        Member member = MemberFixture.member().build();
        memberRepository.save(member);

        Ticket ticket = TicketFixture.ticket()
            .schoolId(1L)
            .ticketType(TicketType.STUDENT)
            .build();
        ticketRepository.save(ticket);

        TicketingRequest request = new TicketingRequest(ticket.getId());

        // when & then
        assertThatThrownBy(() -> ticketingService.ticketing(1L, request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(NEED_STUDENT_VERIFICATION.getMessage());
    }
}
