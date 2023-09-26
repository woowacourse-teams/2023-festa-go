package com.festago.entry.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketValidationRequest(
    @NotBlank(message = "code 는 공백일 수 없습니다.") String code) {

}
