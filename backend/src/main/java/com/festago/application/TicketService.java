package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.StageTicketsResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
import com.festago.dto.TicketingRequest;
import com.festago.exception.BadRequestException;
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

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository,
                         MemberTicketRepository memberTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
        this.memberTicketRepository = memberTicketRepository;
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

    public void ticketing(Member member, TicketingRequest request) {
        Ticket ticket = ticketRepository.findByTicketTypeAndStageId(request.ticketType(), request.stageId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
        MemberTicket memberTicket = ticket.createMemberTicket(member);
        ticket.increaseReservedAmount();
        memberTicketRepository.save(memberTicket);
    }

    //TODO
    @Transactional(readOnly = true)
    public StageTicketsResponse findStageTickets(Long stageId) {
        return null;
    }
}
