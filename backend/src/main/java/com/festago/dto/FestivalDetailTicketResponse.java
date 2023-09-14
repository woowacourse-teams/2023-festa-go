package com.festago.dto;

import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketType;

public record FestivalDetailTicketResponse(
    Long id,
    TicketType ticketType,
    Integer totalAmount,
    Integer remainAmount) {

    public static FestivalDetailTicketResponse from(Ticket ticket) {
        TicketAmount ticketAmount = ticket.getTicketAmount();
        return new FestivalDetailTicketResponse(
            ticket.getId(),
            ticket.getTicketType(),
            ticketAmount.getTotalAmount(),
            ticketAmount.calculateRemainAmount()
        );
    }
}
