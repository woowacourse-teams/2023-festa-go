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
        int remainAmount = ticketAmount.getTotalAmount() - ticketAmount.getReservedAmount();
        return new StageTicketResponse(
            ticket.getId(),
            ticket.getTicketType(),
            ticketAmount.getTotalAmount(),
            remainAmount // TODO : 169 pr 머지 후 ticketAmount.calculateRemainAmount 사용
        );
    }
}
