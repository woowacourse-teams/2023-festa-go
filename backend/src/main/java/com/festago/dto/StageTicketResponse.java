package com.festago.dto;

import com.festago.domain.TicketType;

public record StageTicketResponse(TicketType ticketType, Integer totalAmount, Integer remainAmount) {

}
