package com.festago.dto;

import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketType;

public record FestivalDetailTicketResponse(Long id,
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
