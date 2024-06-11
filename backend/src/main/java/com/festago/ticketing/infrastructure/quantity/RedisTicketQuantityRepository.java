package com.festago.ticketing.infrastructure.quantity;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketQuantity;
import com.festago.ticketing.repository.TicketQuantityRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTicketQuantityRepository implements TicketQuantityRepository {

    private static final String KEY_PREFIX = "ticket_quantity_";
    private final RedisTemplate<String, String> redisTemplate;
    private final Clock clock;

    @Override
    public TicketQuantity put(NewTicket ticket) {
        String key = KEY_PREFIX + ticket.getId();
        int quantity = ticket.getAmount();
        LocalDateTime now = LocalDateTime.now(clock);
        Duration ttl = Duration.between(now, ticket.getTicketingEndTime());
        redisTemplate.opsForValue().set(key, String.valueOf(quantity), ttl);
        return new RedisTicketQuantity(key, redisTemplate);
    }

    @Override
    public Optional<TicketQuantity> findByTicketId(Long ticketId) {
        return Optional.of(new RedisTicketQuantity(KEY_PREFIX + ticketId, redisTemplate));
    }

    @Override
    public void delete(NewTicket ticket) {
        redisTemplate.delete(KEY_PREFIX + ticket.getId());
    }
}
