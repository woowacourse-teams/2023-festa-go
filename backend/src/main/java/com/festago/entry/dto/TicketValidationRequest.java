package com.festago.entry.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketValidationRequest(
    @NotBlank(message = "code는 공백일 수 없습니다.") String code) {

}
