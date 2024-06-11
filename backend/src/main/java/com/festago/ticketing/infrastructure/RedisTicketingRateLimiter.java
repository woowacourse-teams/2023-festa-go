package com.festago.ticketing.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.TicketingRateLimiter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTicketingRateLimiter implements TicketingRateLimiter {

    private static final String KEY_PREFIX = "ticketing_limiter_";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isFrequentTicketing(Booker booker, long timeout, TimeUnit unit) {
        if (timeout <= 0) {
            return false;
        }
        String key = KEY_PREFIX + booker.getMemberId();
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", timeout, unit);
        if (result == null) {
            throw new InternalServerException(ErrorCode.REDIS_ERROR);
        }
        return !result;
    }
}
