package com.festago.dto;

import com.festago.domain.Ticket;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketType;

public record StageTicketResponse(Long id,
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

    public StageTicketResponse setRemainAmount(int remainAmount) {
        return new StageTicketResponse(
            this.id,
            this.ticketType,
            this.totalAmount,
            remainAmount
        );
    }
}
