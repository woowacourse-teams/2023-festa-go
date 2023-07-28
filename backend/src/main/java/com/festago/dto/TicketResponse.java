package com.festago.dto;

import com.festago.domain.Ticket;
import com.festago.domain.TicketType;
import java.time.LocalDateTime;

public record TicketResponse(Long id,
                             Long stageId,
                             TicketType ticketType,
                             Integer totalAmount,
                             LocalDateTime entryTime) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getId(),
            ticket.getStage().getId(),
            ticket.getTicketType(),
            ticket.getTotalAmount(),
            ticket.getEntryTime());
    }
}
