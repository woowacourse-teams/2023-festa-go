package com.festago.stage.dto;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record StageUpdateRequest(
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startTime,
    @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime ticketOpenTime,
    String lineUp
) {

}
