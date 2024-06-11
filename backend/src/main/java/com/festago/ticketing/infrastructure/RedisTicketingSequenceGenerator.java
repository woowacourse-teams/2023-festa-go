package com.festago.ticketing.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.ticketing.domain.TicketingSequenceGenerator;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTicketingSequenceGenerator implements TicketingSequenceGenerator {

    private static final String KEY_PREFIX = "ticketing_seq_";
    private final RedisTemplate<String, String> redisTemplate;
    private final Clock clock;

    /**
     * TTL을 설정하기 위한 메서드 <br/> RedisTicketingSequenceEventListener에서 호출하도록 설계했으니, 특별한 이유가 아니면 직접 호출하는 것을 금지함 <br/>
     *
     * @param ticketId     TTL을 설정할 티켓의 식별자
     * @param invalidateAt 만료될 시간
     */
    protected void setUp(Long ticketId, LocalDateTime invalidateAt) {
        LocalDateTime now = LocalDateTime.now(clock);
        Duration ttl = Duration.between(now, invalidateAt);
        redisTemplate.opsForValue().set(KEY_PREFIX + ticketId, "0", ttl);
    }

    /**
     * 레디스에 저장된 시퀀스를 삭제하기 위한 메서드 <br/> RedisTicketingSequenceEventListener에서 호출하도록 설계했으니, 특별한 이유가 아니면 직접 호출하는 것을 금지함
     * <br/>
     *
     * @param ticketId 삭제할 티켓의 식별자
     */
    protected void delete(Long ticketId) {
        redisTemplate.delete(KEY_PREFIX + ticketId);
    }

    @Override
    public int generate(Long ticketId) {
        Long sequence = redisTemplate.opsForValue().increment(KEY_PREFIX + ticketId);
        if (sequence == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
        return sequence.intValue();
    }
}
