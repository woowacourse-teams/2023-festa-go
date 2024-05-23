package com.festago.stage.dto.command;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record StageUpdateCommand(
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    List<Long> artistIds
) {

}
