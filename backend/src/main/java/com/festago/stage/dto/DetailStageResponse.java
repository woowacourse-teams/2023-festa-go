package com.festago.stage.dto;

import com.festago.stage.domain.Stage;
import com.festago.ticket.dto.DetailTicketResponse;
import java.time.LocalDateTime;
import java.util.List;

public record DetailStageResponse(
    Long id,
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    String lineUp,
    List<DetailTicketResponse> tickets) {

    public static DetailStageResponse from(Stage stage) {
        List<DetailTicketResponse> tickets = stage.getTickets().stream()
            .map(DetailTicketResponse::from)
            .toList();
        return new DetailStageResponse(
            stage.getId(),
            stage.getStartTime(),
            stage.getTicketOpenTime(),
            stage.getLineUp(),
            tickets
        );
    }
}
