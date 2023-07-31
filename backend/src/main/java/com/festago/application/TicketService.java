package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketAmountRepository;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.PoohDto;
import com.festago.dto.StageTicketsResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
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

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository,
                         MemberTicketRepository memberTicketRepository, TicketAmountRepository ticketAmountRepository) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
        this.memberTicketRepository = memberTicketRepository;
        this.ticketAmountRepository = ticketAmountRepository;
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

    public void ticketing(Member member, PoohDto request) {
        int reservedAmount = getReservedAmount(request.ticketId());
        Ticket ticket = ticketRepository.findById(request.ticketId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        MemberTicket memberTicket = ticket.createMemberTicket(member, reservedAmount);
        memberTicketRepository.save(memberTicket);
    }

    private int getReservedAmount(Long ticketId) {
        TicketAmount ticketAmount = ticketAmountRepository.findByTicketIdForUpdate(ticketId) // TicketId로 검색
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        ticketAmount.increaseReservedAmount();
        return ticketAmount.getReservedAmount();
    }

    //TODO
    @Transactional(readOnly = true)
    public StageTicketsResponse findStageTickets(Long stageId) {
        return null;
    }
}
