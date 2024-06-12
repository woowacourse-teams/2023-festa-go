package com.festago.ticket.domain;

import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.ReserveTicket;
import java.time.LocalDateTime;

public class FakeTicket extends NewTicket {

    public FakeTicket(Long id, int quantity) {
        super(id, 1L, TicketExclusive.NONE);
        changeAmount(quantity);
    }

    @Override
    public void validateReserve(Booker booker, LocalDateTime currentTime) {
        // NOOP
    }

    @Override
    public ReserveTicket reserve(Booker booker, int sequence) {
        return null; // NOOP
    }

    @Override
    public LocalDateTime getTicketingEndTime() {
        return LocalDateTime.now().plusHours(1);
    }
}
