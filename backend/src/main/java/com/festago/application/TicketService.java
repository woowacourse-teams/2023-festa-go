package com.festago.application;

import com.festago.domain.TicketRepository;
import com.festago.dto.StageTicketsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public StageTicketsResponse findStageTickets(Long stageId) {
        return null;
    }
}
