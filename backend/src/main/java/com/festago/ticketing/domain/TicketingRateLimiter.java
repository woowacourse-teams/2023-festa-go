package com.festago.ticketing.domain;

import java.util.concurrent.TimeUnit;

public interface TicketingRateLimiter {

    boolean isFrequentTicketing(Booker booker, long timeout, TimeUnit unit);
}
