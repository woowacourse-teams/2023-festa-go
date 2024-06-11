package com.festago.ticket.dto.command;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StageTicketDeleteCommand(
    Long schoolId,
    Long stageTicketId,
    LocalDateTime entryTime
) {

}
