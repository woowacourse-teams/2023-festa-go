package com.festago.dto;

import com.festago.domain.Ticket;

public record TicketCreateResponse(
    Long id) {

    public static TicketCreateResponse from(Ticket ticket) {
        return new TicketCreateResponse(ticket.getId());
    }
}
