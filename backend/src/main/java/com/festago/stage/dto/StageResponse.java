package com.festago.stage.dto;

import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

public record StageResponse(
    Long id,
    LocalDateTime startTime) {

    public static StageResponse from(Stage stage) {
        return new StageResponse(stage.getId(), stage.getStartTime());
    }
}
