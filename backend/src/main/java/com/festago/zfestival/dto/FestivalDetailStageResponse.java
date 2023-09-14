package com.festago.zfestival.dto;

import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;
import java.util.List;

public record FestivalDetailStageResponse(
    Long id,
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    String lineUp,
    List<FestivalDetailTicketResponse> tickets) {

    public static FestivalDetailStageResponse from(Stage stage) {
        List<FestivalDetailTicketResponse> tickets = stage.getTickets().stream()
            .map(FestivalDetailTicketResponse::from)
            .toList();
        return new FestivalDetailStageResponse(
            stage.getId(),
            stage.getStartTime(),
            stage.getTicketOpenTime(),
            stage.getLineUp(),
            tickets
        );
    }
}
