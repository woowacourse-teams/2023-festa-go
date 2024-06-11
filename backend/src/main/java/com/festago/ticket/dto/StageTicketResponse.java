package com.festago.ticket.dto;

import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketType;

@Deprecated(forRemoval = true)
public record StageTicketResponse(
    Long id,
    TicketType ticketType,
    Integer totalAmount,
    Integer remainAmount) {

    public static StageTicketResponse from(Ticket ticket) {
        TicketAmount ticketAmount = ticket.getTicketAmount();
        return new StageTicketResponse(
            ticket.getId(),
            ticket.getTicketType(),
            ticketAmount.getTotalAmount(),
            ticketAmount.calculateRemainAmount()
        );
    }
}
