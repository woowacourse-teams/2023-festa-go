package com.festago.dto;

import com.festago.domain.Stage;
import java.time.LocalDateTime;

public record CurrentStageResponse(Long id,
                                   LocalDateTime startTime) {

    public static CurrentStageResponse from(Stage stage) {
        return new CurrentStageResponse(
            stage.getId(),
            stage.getStartTime()
        );
    }
}
