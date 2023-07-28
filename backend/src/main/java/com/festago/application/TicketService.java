package com.festago.application;

import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StageRepository stageRepository;

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
    }

    public TicketResponse create(TicketCreateRequest request) {
        Stage stage = findStageById(request.stageId());
        Ticket newTicket = ticketRepository.save(new Ticket(stage,
            request.ticketType(),
            request.totalAmount(),
            request.entryTime()));

        return TicketResponse.from(newTicket);
    }

    private Stage findStageById(Long stageId) {
        return stageRepository.findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }
}
