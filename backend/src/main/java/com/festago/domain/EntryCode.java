package com.festago.domain;

public class EntryCode {

    private static final long DEFAULT_PERIOD = 30;
    private static final long DEFAULT_OFFSET = 10;

    private final String code;
    private final long period;
    private final long offset;

    public EntryCode(String code, long period, long offset) {
        this.code = code;
        this.period = period;
        this.offset = offset;
    }

    public static EntryCode create(EntryCodeProvider entryCodeProvider, MemberTicket memberTicket) {
        String code = entryCodeProvider.provide(memberTicket, DEFAULT_PERIOD + DEFAULT_OFFSET);
        return new EntryCode(code, DEFAULT_PERIOD, DEFAULT_OFFSET);
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
