package com.festago.stage.dto.event;

import com.festago.stage.domain.Stage;

public record StageCreatedEvent(
    Stage stage
) {

}
