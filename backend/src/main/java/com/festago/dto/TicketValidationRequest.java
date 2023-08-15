package com.festago.dto;

import jakarta.validation.constraints.NotNull;

public record TicketValidationRequest(@NotNull String code) {

}
