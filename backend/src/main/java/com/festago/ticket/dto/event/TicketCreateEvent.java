package com.festago.ticket.dto.event;

import java.time.LocalDateTime;

public record TicketCreateEvent(
    Long stageId,
    LocalDateTime entryTime
) {

}
