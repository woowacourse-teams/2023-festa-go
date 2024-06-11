package com.festago.ticketing.application.command;

import com.festago.ticketing.dto.TicketingResult;
import com.festago.ticketing.dto.command.TicketingCommand;

public interface TicketingCommandService {

    TicketingResult reserveTicket(TicketingCommand command);
}
