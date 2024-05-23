package com.festago.stage.dto.command;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record StageCreateCommand(
    Long festivalId,
    LocalDateTime startTime,
    LocalDateTime ticketOpenTime,
    List<Long> artistIds
) {

}
