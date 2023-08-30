package com.festago.domain;

public class EntryCode {

    private final String code;
    private final EntryCodeTime entryCodeTime;

    public EntryCode(String code, EntryCodeTime entryCodeTime) {
        this.code = code;
        this.entryCodeTime = entryCodeTime;
    }

    public String getCode() {
        return code;
    }

    public long getPeriod() {
        return entryCodeTime.getPeriod();
    }

    public long getOffset() {
        return entryCodeTime.getOffset();
    }
}
