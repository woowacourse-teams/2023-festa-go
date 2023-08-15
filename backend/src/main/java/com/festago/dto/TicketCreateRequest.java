package com.festago.dto;

import com.festago.domain.TicketType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record TicketCreateRequest(
    @NotNull Long stageId,
    @NotNull TicketType ticketType,
    @NotNull Integer amount,
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime entryTime) {

}
