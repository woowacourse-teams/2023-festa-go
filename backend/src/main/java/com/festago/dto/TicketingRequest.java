package com.festago.dto;

import jakarta.validation.constraints.NotNull;

public record TicketingRequest(
    @NotNull(message = "ticketId 는 null 일 수 없습니다.") Long ticketId) {

}
