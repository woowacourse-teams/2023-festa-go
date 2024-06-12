package com.festago.ticketing.application.command;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.repository.TicketSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketSequenceUpdateService {

    private final TicketSequenceRepository ticketSequenceRepository;

    public void putOrDeleteTicketSequence(NewTicket ticket) {
        if (ticket.isEmptyAmount()) {
            ticketSequenceRepository.delete(ticket);
        } else {
            ticketSequenceRepository.put(ticket);
        }
    }
}
