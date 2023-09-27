package com.festago.stage.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record StageUpdateRequest(
    @NotNull(message = "startTime는 null일 수 없습니다.") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startTime,
    @NotNull(message = "ticketOpenTime는 null일 수 없습니다.") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime ticketOpenTime,
    String lineUp
) {

}
