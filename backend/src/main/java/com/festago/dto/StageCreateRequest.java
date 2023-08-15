package com.festago.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record StageCreateRequest(
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startTime,
    String lineUp,
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime ticketOpenTime,
    @NotNull Long festivalId) {

}
