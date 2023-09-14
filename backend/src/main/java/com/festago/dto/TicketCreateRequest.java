package com.festago.dto;

import com.festago.domain.TicketType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record TicketCreateRequest(
    @NotNull(message = "stageId 는 null 일 수 없습니다.") Long stageId,
    @NotNull(message = "ticketType 은 null 일 수 없습니다.") TicketType ticketType,
    @NotNull(message = "amount 는 null 일 수 없습니다.") Integer amount,
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime entryTime) {

}
