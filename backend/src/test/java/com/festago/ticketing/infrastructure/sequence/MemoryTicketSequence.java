package com.festago.ticketing.infrastructure.sequence;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticketing.domain.TicketSequence;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class MemoryTicketSequence implements TicketSequence {

    private final Queue<Integer> queue;

    public MemoryTicketSequence(int quantity) {
        this.queue = new ArrayBlockingQueue<>(quantity);
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
        queue.offer(sequence);
    }

    @Override
    public int getQuantity() {
        return queue.size();
    }
}
