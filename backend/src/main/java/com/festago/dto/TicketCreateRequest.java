package com.festago.dto;

import com.festago.domain.TicketType;
import java.time.LocalDateTime;

public record TicketCreateRequest(Long stageId, TicketType ticketType, Integer amount, LocalDateTime entryTime) {

}
