package com.festago.common.infrastructure;

import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.TicketingRateLimiter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeTicketingRateLimiter implements TicketingRateLimiter {

    private final boolean isFrequentReserve;

    @Override
    public boolean isFrequentTicketing(Booker booker, long timeout, TimeUnit unit) {
        return isFrequentReserve;
    }
}
