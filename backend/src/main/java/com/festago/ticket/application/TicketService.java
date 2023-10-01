package com.festago.ticket.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.StageTicketsResponse;
import com.festago.ticket.dto.TicketCreateRequest;
import com.festago.ticket.dto.TicketCreateResponse;
import com.festago.ticket.dto.event.TicketCreateEvent;
import com.festago.ticket.repository.TicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StageRepository stageRepository;
    private final Clock clock;
    private final ApplicationEventPublisher publisher;

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository, Clock clock,
                         ApplicationEventPublisher publisher) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
        this.clock = clock;
        this.publisher = publisher;
    }

    public TicketCreateResponse create(TicketCreateRequest request) {
        Stage stage = findStageById(request.stageId());
        TicketType ticketType = request.ticketType();
        School school = stage.getFestival().getSchool();

        Ticket ticket = ticketRepository.findByTicketTypeAndStage(ticketType, stage)
            .orElseGet(() -> ticketRepository.save(new Ticket(stage, ticketType, school)));

        ticket.addTicketEntryTime(LocalDateTime.now(clock), request.entryTime(), request.amount());

        publisher.publishEvent(new TicketCreateEvent(request.stageId(), request.entryTime()));
        return TicketCreateResponse.from(ticket);
    }

    private Stage findStageById(Long stageId) {
        return stageRepository.findByIdWithFetch(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public StageTicketsResponse findStageTickets(Long stageId) {
        return StageTicketsResponse.from(ticketRepository.findAllByStageId(stageId));
    }
}
