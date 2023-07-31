package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketAmountRepository;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.StageTicketsResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StageRepository stageRepository;
    private final MemberTicketRepository memberTicketRepository;
    private final TicketAmountRepository ticketAmountRepository;
    private final MemberRepository memberRepository;

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository,
                         MemberTicketRepository memberTicketRepository,
                         TicketAmountRepository ticketAmountRepository,
                         MemberRepository memberRepository) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
        this.memberTicketRepository = memberTicketRepository;
        this.ticketAmountRepository = ticketAmountRepository;
        this.memberRepository = memberRepository;
    }

    public TicketCreateResponse create(TicketCreateRequest request) {
        Stage stage = findStageById(request.stageId());
        TicketType ticketType = request.ticketType();

        Ticket ticket = ticketRepository.findByTicketTypeAndStage(ticketType, stage)
            .orElseGet(() -> ticketRepository.save(new Ticket(stage, ticketType)));

        ticket.addTicketEntryTime(request.entryTime(), request.amount());

        return TicketCreateResponse.from(ticket);
    }

    private Stage findStageById(Long stageId) {
        return stageRepository.findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }

    public TicketingResponse ticketing(Long memberId, TicketingRequest request) {
        TicketAmount ticketAmount = ticketAmountRepository.findByTicketIdForUpdate(request.ticketId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        ticketAmount.increaseReservedAmount();
        int reservedAmount = ticketAmount.getReservedAmount();
        System.out.println("reservedAmount = " + reservedAmount);
        Ticket ticket = ticketRepository.findById(request.ticketId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        MemberTicket memberTicket = memberTicketRepository.save(ticket.createMemberTicket(member, reservedAmount));
        return TicketingResponse.from(memberTicket);
    }

    //TODO
    @Transactional(readOnly = true)
    public StageTicketsResponse findStageTickets(Long stageId) {
        return null;
    }
}
