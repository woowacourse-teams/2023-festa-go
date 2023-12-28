package com.festago.ticketing.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.student.repository.StudentRepository;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketAmountRepository;
import com.festago.ticket.repository.TicketRepository;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.TicketingRequest;
import com.festago.ticketing.dto.TicketingResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketingService {

    private final MemberTicketRepository memberTicketRepository;
    private final TicketAmountRepository ticketAmountRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final Clock clock;

    public TicketingResponse ticketing(Long memberId, TicketingRequest request) {
        Ticket ticket = findTicketById(request.ticketId());
        Member member = findMemberById(memberId);
        validateAlreadyReserved(member, ticket);
        validateStudent(member, ticket);
        int reserveSequence = getReserveSequence(request.ticketId());
        MemberTicket memberTicket = MemberTicket.createMemberTicket(ticket, member, reserveSequence,
            LocalDateTime.now(clock));
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

    private void validateStudent(Member member, Ticket ticket) {
        if (ticket.getTicketType() != TicketType.STUDENT) {
            return;
        }
        if (!studentRepository.existsByMemberAndSchoolId(member, ticket.getSchoolId())) {
            throw new BadRequestException(ErrorCode.NEED_STUDENT_VERIFICATION);
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
