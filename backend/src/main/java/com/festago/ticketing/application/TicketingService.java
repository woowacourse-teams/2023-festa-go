package com.festago.ticketing.application;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.repository.TicketAmountRepository;
import com.festago.ticket.repository.TicketRepository;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.TicketingRequest;
import com.festago.ticketing.dto.TicketingResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import com.festago.zmember.domain.Member;
import com.festago.zmember.repository.MemberRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketingService {

    private final MemberTicketRepository memberTicketRepository;
    private final TicketAmountRepository ticketAmountRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public TicketingService(MemberTicketRepository memberTicketRepository,
                            TicketAmountRepository ticketAmountRepository,
                            TicketRepository ticketRepository, MemberRepository memberRepository, Clock clock) {
        this.memberTicketRepository = memberTicketRepository;
        this.ticketAmountRepository = ticketAmountRepository;
        this.ticketRepository = ticketRepository;
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public TicketingResponse ticketing(Long memberId, TicketingRequest request) {
        Ticket ticket = findTicketById(request.ticketId());
        Member member = findMemberById(memberId);
        validateAlreadyReserved(member, ticket);
        int reserveSequence = getReserveSequence(request.ticketId());
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

    private int getReserveSequence(Long ticketId) {
        TicketAmount ticketAmount = findTicketAmountById(ticketId);
        ticketAmount.increaseReservedAmount();
        return ticketAmount.getReservedAmount();
    }

    private TicketAmount findTicketAmountById(Long ticketId) {
        return ticketAmountRepository.findByTicketIdForUpdate(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
    }
}
