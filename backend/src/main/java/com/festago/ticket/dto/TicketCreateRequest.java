package com.festago.ticket.dto;

import com.festago.ticket.domain.TicketType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record TicketCreateRequest(
    @NotNull(message = "stageId는 null 일 수 없습니다.")
    Long stageId,
    @NotNull(message = "ticketType은 null 일 수 없습니다.")
    TicketType ticketType,
    @NotNull(message = "amount는 null 일 수 없습니다.")
    Integer amount,
    @NotNull(message = "entryTime은 null 일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime entryTime
) {

}
