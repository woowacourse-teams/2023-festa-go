package com.festago.support;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeInstantProvider {

    public static Instant from(String localDateTime) {
        return from(LocalDateTime.parse(localDateTime));
    }

    public static Instant from(LocalDateTime localDateTime) {
        return Instant.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    public static Instant from(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
