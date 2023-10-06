package com.festago.entry.domain;

import org.springframework.util.StringUtils;

public class EntryCode {

    private static final int MINIMUM_PERIOD = 0;
    private static final int MINIMUM_OFFSET = 0;

    private final String code;
    private final long period;
    private final long offset;

    public EntryCode(String code, long period, long offset) {
        validate(code, period, offset);
        this.code = code;
        this.period = period;
        this.offset = offset;
    }

    private void validate(String code, long period, long offset) {
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("code는 빈 값 또는 null이 될 수 없습니다.");
        }
        if (period <= MINIMUM_PERIOD) {
            throw new IllegalArgumentException("period는 0 또는 음수가 될 수 없습니다.");
        }
        if (isNegative(offset)) {
            throw new IllegalArgumentException("offset은 음수가 될 수 없습니다.");
        }
    }

    private boolean isNegative(long offset) {
        return offset < MINIMUM_OFFSET;
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
