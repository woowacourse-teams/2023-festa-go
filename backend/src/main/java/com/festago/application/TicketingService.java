package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketAmountRepository;
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
        return ticketRepository.findById(ticketId)
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
