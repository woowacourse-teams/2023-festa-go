package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketingService {

    private final TicketAmountService ticketAmountService;
    private final MemberTicketRepository memberTicketRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public TicketingService(TicketAmountService redisTicketAmountService, MemberTicketRepository memberTicketRepository,
                            TicketRepository ticketRepository, MemberRepository memberRepository, Clock clock) {
        this.ticketAmountService = redisTicketAmountService;
        this.memberTicketRepository = memberTicketRepository;
        this.ticketRepository = ticketRepository;
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public TicketingResponse ticketing(Long memberId, TicketingRequest request) {
        Ticket ticket = findTicketById(request.ticketId());
        Member member = findMemberById(memberId);
        validateAlreadyReserved(member, ticket);
        Integer reserveSequence = ticketAmountService.getSequence(ticket, member)
            .orElseThrow(() -> new BadRequestException(ErrorCode.TICKET_SOLD_OUT));
        MemberTicket memberTicket = ticket.createMemberTicket(member, reserveSequence, LocalDateTime.now(clock));
        memberTicketRepository.save(memberTicket);
        return TicketingResponse.from(memberTicket);
    }

    private Ticket findTicketById(Long ticketId) {
        return ticketRepository.findByIdWithFetch(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateAlreadyReserved(Member member, Ticket ticket) {
        if (memberTicketRepository.existsByOwnerAndStage(member, ticket.getStage())) {
            throw new BadRequestException(ErrorCode.RESERVE_TICKET_OVER_AMOUNT);
        }
    }
}
