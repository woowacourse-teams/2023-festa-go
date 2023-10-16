package com.festago.admin.dto;

import com.festago.ticket.domain.TicketType;
import java.time.LocalDateTime;
import java.util.Map;

public record AdminTicketResponse(
    Long id,
    Long stageId,
    TicketType ticketType,
    Integer totalAmount,
    Integer reservedAmount,
    Map<LocalDateTime, Integer> entryTimeAmount) {

}
