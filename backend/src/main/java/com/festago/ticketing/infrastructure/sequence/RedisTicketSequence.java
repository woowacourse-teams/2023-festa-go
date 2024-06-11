package com.festago.ticketing.infrastructure.sequence;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.ticketing.domain.TicketSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisTicketSequence implements TicketSequence {

    private final String key;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isSoldOut() {
        Long size = redisTemplate.opsForList().size(key);
        if (size == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
        return size <= 0;
    }

    @Override
    public int reserve() {
        String sequence = redisTemplate.opsForList().rightPop(key);
        if (sequence == null) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        return Integer.parseInt(sequence);
    }

    @Override
    public void cancel(int sequence) {
        redisTemplate.opsForList().leftPush(key, String.valueOf(sequence));
    }

    @Override
    public int getQuantity() {
        Long size = redisTemplate.opsForList().size(key);
        if (size == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
        return size.intValue();
    }
}
