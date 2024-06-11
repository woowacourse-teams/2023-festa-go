package com.festago.ticketing.repository;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketQuantity;
import com.festago.ticketing.infrastructure.MemoryTicketQuantity;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryTicketQuantityRepository implements TicketQuantityRepository {

    private final ConcurrentHashMap<Long, TicketQuantity> memory = new ConcurrentHashMap<>();

    @Override
    public TicketQuantity put(NewTicket ticket) {
        TicketQuantity ticketQuantity = new MemoryTicketQuantity(ticket.getAmount());
        memory.put(ticket.getId(), ticketQuantity);
        return ticketQuantity;
    }

    @Override
    public Optional<TicketQuantity> findByTicketId(Long ticketId) {
        return Optional.ofNullable(memory.get(ticketId));
    }

    @Override
    public void delete(NewTicket ticket) {
        memory.remove(ticket.getId());
    }
}
