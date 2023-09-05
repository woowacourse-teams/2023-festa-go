package com.festago.application;

import com.festago.event.TicketAmountChangeEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TicketAmountListener {

    private final RedisTemplate<String, String> redisTemplate;

    public TicketAmountListener(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @TransactionalEventListener
    public void updateTicketAmount(TicketAmountChangeEvent ticketAmountChangeEvent) {
        Long ticketId = ticketAmountChangeEvent.ticketId();
        Integer ticketAmount = ticketAmountChangeEvent.ticketAmount();
        redisTemplate.opsForValue().set("ticketAmount_" + ticketId.toString(), ticketAmount.toString());
    }
}
