package com.festago.entry.dto;

import jakarta.validation.constraints.NotNull;

public record TicketValidationRequest(
    @NotNull(message = "code 는 null 일 수 없습니다.") String code) {

}
