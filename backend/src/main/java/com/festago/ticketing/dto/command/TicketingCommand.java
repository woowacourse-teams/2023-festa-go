package com.festago.ticketing.dto.command;

import com.festago.ticket.domain.NewTicketType;
import com.festago.ticketing.domain.Booker;
import lombok.Builder;

@Builder
public record TicketingCommand(
    Long ticketId,
    NewTicketType ticketType,
    Booker booker
) {

}
