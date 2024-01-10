package com.festago.ticket.repository;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryTicketRepository implements TicketRepository {

    private final MemoryTicketAmountRepository ticketAmountRepository;
    private final Map<Long, Ticket> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public MemoryTicketRepository(MemoryTicketAmountRepository ticketAmountRepository) {
        this.ticketAmountRepository = ticketAmountRepository;
    }

    @SneakyThrows
    @Override
    public Ticket save(Ticket ticket) {
        Field idField = ticket.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(ticket, autoIncrement.incrementAndGet());
        memory.put(ticket.getId(), ticket);
        ticketAmountRepository.save(ticket);
        return ticket;
    }

    @Override
    public long count() {
        return memory.size();
    }

    @Override
    public List<Ticket> findAllByStageIdWithFetch(Long stageId) {
        return memory.values().stream()
            .filter(ticket -> ticket.getStage().getId().equals(stageId))
            .toList();
    }

    @Override
    public Optional<Ticket> findByIdWithFetch(Long ticketId) {
        return Optional.ofNullable(memory.get(ticketId));
    }

    @Override
    public Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage) {
        return memory.values().stream()
            .filter(
                ticket -> ticket.getTicketType().equals(ticketType) && ticket.getStage().getId().equals(stage.getId()))
            .findAny();
    }

    public void clear() {
        memory.clear();
    }
}
