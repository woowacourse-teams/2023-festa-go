package com.festago.ticket.dto;

import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketType;

public record DetailTicketResponse(
    Long id,
    TicketType ticketType,
    Integer totalAmount,
    Integer remainAmount) {

    public static DetailTicketResponse from(Ticket ticket) {
        TicketAmount ticketAmount = ticket.getTicketAmount();
        return new DetailTicketResponse(
            ticket.getId(),
            ticket.getTicketType(),
            ticketAmount.getTotalAmount(),
            ticketAmount.calculateRemainAmount()
        );
    }
}
