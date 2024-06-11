package com.festago.ticketing.infrastructure;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticketing.domain.TicketQuantity;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTicketQuantity implements TicketQuantity {

    private final AtomicInteger quantity;

    public MemoryTicketQuantity(int quantity) {
        this.quantity = new AtomicInteger(quantity);
    }

    @Override
    public boolean isSoldOut() {
        return quantity.get() <= 0;
    }

    @Override
    public void decreaseQuantity() {
        int remainAmount = quantity.decrementAndGet();
        if (remainAmount < 0) { // 티켓 재고 음수 방지
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
    }

    @Override
    public void increaseQuantity() {
        quantity.incrementAndGet();
    }

    @Override
    public int getQuantity() {
        return quantity.get();
    }
}
