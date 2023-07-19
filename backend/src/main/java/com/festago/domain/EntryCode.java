package com.festago.domain;

import java.util.Date;

public class EntryCode {

    private static final long DEFAULT_PERIOD = 30;
    private static final long DEFAULT_OFFSET = 10;
    private static final int MILLISECOND_FACTOR = 1000;
    private static final int MINIMUM_PERIOD = 0;

    private final String code;
    private final long period;
    private final long offset;

    public EntryCode(String code, long period, long offset) {
        validate(period);
        this.code = code;
        this.period = period;
        this.offset = offset;
    }

    public static EntryCode create(EntryCodeProvider entryCodeProvider, MemberTicket memberTicket) {
        Date expiredAt = new Date(new Date().getTime() + (DEFAULT_PERIOD + DEFAULT_OFFSET) * MILLISECOND_FACTOR);
        String code = entryCodeProvider.provide(EntryCodePayload.from(memberTicket), expiredAt);
        return new EntryCode(code, DEFAULT_PERIOD, DEFAULT_OFFSET);
    }

    public void validate(long period) {
        if (period <= MINIMUM_PERIOD) {
            throw new IllegalArgumentException(); // TODO
        }
    }

    public String getCode() {
        return code;
    }

    public long getPeriod() {
        return period;
    }

    public long getOffset() {
        return offset;
    }
}
