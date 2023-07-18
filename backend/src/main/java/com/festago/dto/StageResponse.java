package com.festago.dto;

import com.festago.domain.Stage;
import java.time.LocalDateTime;

public record StageResponse(Long id, String name, LocalDateTime startTime) {

    public static StageResponse from(Stage stage) {
        return new StageResponse(stage.getId(), stage.getName(), stage.getStartTime());
    }
}
