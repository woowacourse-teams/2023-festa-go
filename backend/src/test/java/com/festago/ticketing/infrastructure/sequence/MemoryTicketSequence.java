package com.festago.ticketing.infrastructure.sequence;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticketing.domain.TicketSequence;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.IntStream;

public class MemoryTicketSequence implements TicketSequence {

    private final Deque<Integer> queue;

    public MemoryTicketSequence(int quantity) {
        this.queue = new LinkedBlockingDeque<>(quantity);
        List<Integer> sequences = IntStream.rangeClosed(1, quantity)
            .boxed()
            .toList();
        queue.addAll(sequences);
    }

    @Override
    public boolean isSoldOut() {
        return queue.isEmpty();
    }

    @Override
    public int reserve() {
        Integer sequence = queue.poll();
        if (sequence == null) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        return sequence;
    }

    @Override
    public void cancel(int sequence) {
        queue.addFirst(sequence);
    }

    @Override
    public int getQuantity() {
        return queue.size();
    }
}
