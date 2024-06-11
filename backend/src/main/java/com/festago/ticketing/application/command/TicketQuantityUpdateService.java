package com.festago.ticketing.application.command;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.repository.TicketQuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketQuantityUpdateService {

    private final TicketQuantityRepository ticketQuantityRepository;

    public void putOrDeleteTicketQuantity(NewTicket ticket) {
        if (ticket.isEmptyAmount()) {
            ticketQuantityRepository.delete(ticket);
        } else {
            ticketQuantityRepository.put(ticket);
        }
    }
}
