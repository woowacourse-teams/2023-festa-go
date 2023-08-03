package com.festago.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AdminStageResponse(
    Long id,
    Long festivalId,
    LocalDateTime startTime,
    String lineUp,
    List<Long> ticketId) {

}
