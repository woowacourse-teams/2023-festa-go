package com.festago.stage.dto;

import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

public record StageResponse(
    Long id,
    Long festivalId,
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    String lineUp) {

    public static StageResponse from(Stage stage) {
        return new StageResponse(stage.getId(), stage.getFestival().getId(), stage.getStartTime(),
            stage.getTicketOpenTime(), stage.getLineUp());
    }
}
