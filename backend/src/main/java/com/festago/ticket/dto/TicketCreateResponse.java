package com.festago.ticket.dto;

import com.festago.ticket.domain.Ticket;

public record TicketCreateResponse(
    Long id) {

    public static TicketCreateResponse from(Ticket ticket) {
        return new TicketCreateResponse(ticket.getId());
    }
}
