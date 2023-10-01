package com.festago.stage.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record StageCreateRequest(
    @NotNull(message = "startTime은 null일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime startTime,
    String lineUp,
    @NotNull(message = "ticketOpenTime은 null일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime ticketOpenTime,
    @NotNull(message = "festivalId는 null 일 수 없습니다.")
    Long festivalId
) {

}
