package com.festago.dto;

import com.festago.domain.TicketType;

public record TicketingRequest(Long stageId, TicketType ticketType) {

}
