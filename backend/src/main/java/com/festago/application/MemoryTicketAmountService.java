package com.festago.application;

import com.festago.domain.Ticket;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class MemoryTicketAmountService implements TicketAmountService {

    private final Map<Long, AtomicInteger> ticketAmountMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Integer> getSequence(Ticket ticket) {
        Integer totalAmount = ticket.getTicketAmount().getTotalAmount();
        int quantity = ticketAmountMap.computeIfAbsent(ticket.getId(),
            ignore -> new AtomicInteger(totalAmount)).decrementAndGet();
        if (quantity < 0) {
            return Optional.empty();
        }
        return Optional.of(totalAmount - quantity);
    }

    public void clear() {
        ticketAmountMap.clear();
    }
}
