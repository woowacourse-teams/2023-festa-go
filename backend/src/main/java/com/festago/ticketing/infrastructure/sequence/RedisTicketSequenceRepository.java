package com.festago.ticketing.infrastructure.sequence;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketSequence;
import com.festago.ticketing.repository.TicketSequenceRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTicketSequenceRepository implements TicketSequenceRepository {

    private static final String KEY_PREFIX = "ticketing_seq_queue_";
    private final RedisTemplate<String, String> redisTemplate;
    private final Clock clock;

    @Override
    public TicketSequence put(NewTicket ticket) {
        Long ticketId = ticket.getId();
        String key = KEY_PREFIX + ticketId;
        redisTemplate.delete(key);
        List<String> sequences = IntStream.rangeClosed(1, ticket.getAmount())
            .mapToObj(String::valueOf)
            .toList();
        redisTemplate.opsForList().leftPushAll(key, sequences);
        LocalDateTime now = LocalDateTime.now(clock);
        Duration duration = Duration.between(now, ticket.getTicketingEndTime());
        redisTemplate.expire(key, duration);
        return new RedisTicketSequence(key, redisTemplate);
    }

    @Override
    public Optional<TicketSequence> findByTicketId(Long ticketId) {
        return Optional.of(new RedisTicketSequence(KEY_PREFIX + ticketId, redisTemplate));
    }

    @Override
    public void delete(NewTicket ticket) {
        redisTemplate.delete(KEY_PREFIX + ticket.getId());
    }
}
