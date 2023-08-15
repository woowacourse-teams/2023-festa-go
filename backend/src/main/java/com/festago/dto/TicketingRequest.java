package com.festago.dto;

import jakarta.validation.constraints.NotNull;

public record TicketingRequest(@NotNull Long ticketId) {

}
