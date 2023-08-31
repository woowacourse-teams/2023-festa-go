package com.festago.support;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeInstantProvider {

    public static Instant setCurrentTime(LocalDateTime localDateTime) {
        return Instant.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }
}
