package com.festago.festival.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DetailStageResponse(
    Long id,
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    String lineUp,
    List<DetailTicketResponse> tickets) {

}
