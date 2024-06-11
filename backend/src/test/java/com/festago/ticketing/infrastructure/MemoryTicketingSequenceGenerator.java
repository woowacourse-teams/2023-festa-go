package com.festago.ticketing.infrastructure;

import com.festago.ticketing.domain.TicketingSequenceGenerator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTicketingSequenceGenerator implements TicketingSequenceGenerator {

    private final Map<Long, AtomicInteger> memory = new ConcurrentHashMap<>();

    @Override
    public int generate(Long ticketId) {
        AtomicInteger sequence = memory.computeIfAbsent(ticketId, ignore -> new AtomicInteger());
        return sequence.incrementAndGet();
    }
}
