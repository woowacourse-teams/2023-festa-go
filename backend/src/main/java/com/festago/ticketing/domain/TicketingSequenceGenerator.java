package com.festago.ticketing.domain;

public interface TicketingSequenceGenerator {

    int generate(Long ticketId);
}
