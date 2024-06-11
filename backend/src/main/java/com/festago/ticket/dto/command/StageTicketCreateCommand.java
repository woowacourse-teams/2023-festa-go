package com.festago.ticket.dto.command;

import com.festago.ticket.domain.TicketExclusive;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StageTicketCreateCommand(
    Long schoolId,
    Long stageId,
    TicketExclusive ticketExclusive,
    int amount,
    LocalDateTime entryTime
) {

}
