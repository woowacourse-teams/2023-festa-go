package com.festago.ticketing.dto;

import jakarta.validation.constraints.NotNull;

public record TicketingRequest(
    @NotNull(message = "ticketId는 null 일 수 없습니다.")
    Long ticketId
) {

}
