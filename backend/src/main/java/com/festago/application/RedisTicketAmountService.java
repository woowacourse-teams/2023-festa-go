package com.festago.application;

import com.festago.domain.Ticket;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Profile("!test")
@Service
public class RedisTicketAmountService implements TicketAmountService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTicketAmountService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<Integer> getSequence(Ticket ticket) {
        Integer totalAmount = ticket.getTicketAmount().getTotalAmount();
        Long quantity = getQuantity(ticket.getId());
        if (quantity == null || quantity < 0) {
            return Optional.empty();
        }
        return Optional.of(totalAmount - quantity.intValue());
    }

    private Long getQuantity(Long ticketId) {
        return redisTemplate.opsForValue()
            .decrement("ticketAmount_" + ticketId.toString());
    }
}
