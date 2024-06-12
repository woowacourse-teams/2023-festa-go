package com.festago.ticketing.domain.validator;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.Booker;

public interface TicketingValidator {

    void validate(NewTicket ticket, Booker booker);
}
