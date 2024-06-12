package com.festago.ticketing.repository;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketSequence;
import com.festago.ticketing.infrastructure.sequence.MemoryTicketSequence;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryTicketSequenceRepository implements TicketSequenceRepository {

    private final Map<Long, TicketSequence> memory = new ConcurrentHashMap<>();

    @Override
    public TicketSequence put(NewTicket ticket) {
        MemoryTicketSequence ticketSequence = new MemoryTicketSequence(ticket.getAmount());
        memory.put(ticket.getId(), ticketSequence);
        return ticketSequence;
    }

    @Override
    public Optional<TicketSequence> findByTicketId(Long ticketId) {
        return Optional.ofNullable(memory.get(ticketId));
    }

    @Override
    public void delete(NewTicket ticket) {
        memory.remove(ticket.getId());
    }
}
