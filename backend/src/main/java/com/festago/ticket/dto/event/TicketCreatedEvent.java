package com.festago.ticket.dto.event;

import com.festago.ticket.domain.NewTicket;

public record TicketCreatedEvent(
    NewTicket ticket
) {

}
