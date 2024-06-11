package com.festago.ticketing.infrastructure.quantity;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import com.festago.ticketing.domain.TicketQuantity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@RequiredArgsConstructor
public class RedisTicketQuantity implements TicketQuantity {

    private final String key;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isSoldOut() {
        String quantity = redisTemplate.opsForValue().get(key);
        if (quantity == null) {
            log.warn("존재하지 않는 티켓에 대한 요청이 발생했습니다. key={}", key);
            throw new NotFoundException(ErrorCode.TICKET_NOT_FOUND);
        }
        return Integer.parseInt(quantity) <= 0;
    }

    @Override
    public void decreaseQuantity() {
        Long quantity = redisTemplate.opsForValue().decrement(key);
        if (quantity == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
        if (quantity < 0) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
    }

    @Override
    public void increaseQuantity() {
        Long quantity = redisTemplate.opsForValue().increment(key);
        if (quantity == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
    }

    @Override
    public int getQuantity() {
        String quantity = redisTemplate.opsForValue().get(key);
        if (quantity == null) {
            throw new NotFoundException(ErrorCode.TICKET_NOT_FOUND);
        }
        return Integer.parseInt(quantity);
    }
}
