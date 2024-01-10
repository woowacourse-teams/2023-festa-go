package com.festago.ticket.repository;

import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketAmount;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;

public class MemoryTicketAmountRepository implements TicketAmountRepository {

    private final Map<Long, TicketAmount> memory = new ConcurrentHashMap<>();

    @Override
    public Optional<TicketAmount> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public Optional<TicketAmount> findByTicketIdForUpdate(Long ticketId) {
        return findById(ticketId);
    }

    @SneakyThrows
    public TicketAmount save(Ticket ticket) {
        assert ticket.getId() != null;
        TicketAmount ticketAmount = ticket.getTicketAmount();
        Field idField = ticketAmount.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(ticketAmount, ticket.getId());
        memory.put(ticketAmount.getId(), ticketAmount);
        return ticketAmount;
    }

    public void clear() {
        memory.clear();
    }
}
