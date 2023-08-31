package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.util.Date;

public class EntryCodeTime {

    private static final EntryCodeTime DEFAULT_ENTRY_TIME = new EntryCodeTime(30, 10);
    private static final int MILLISECOND_FACTOR = 1000;
    private static final int MINIMUM_PERIOD = 0;
    private static final int MINIMUM_OFFSET = 0;

    private final long period;
    private final long offset;

    private EntryCodeTime(long period, long offset) {
        validate(period, offset);
        this.period = period;
        this.offset = offset;
    }

    public static EntryCodeTime create() {
        return DEFAULT_ENTRY_TIME;
    }

    public static EntryCodeTime of(long period, long offset) {
        return new EntryCodeTime(period, offset);
    }

    private void validate(long period, long offset) {
        if (period <= MINIMUM_PERIOD) {
            throw new InternalServerException(ErrorCode.INVALID_ENTRY_CODE_PERIOD);
        }
        if (isNegative(offset)) {
            throw new InternalServerException(ErrorCode.INVALID_ENTRY_CODE_OFFSET);
        }
    }

    private boolean isNegative(long offset) {
        return offset < MINIMUM_OFFSET;
    }

    public Date getExpiredAt(long currentTimeMillis) {
        return new Date(currentTimeMillis + (period + offset) * MILLISECOND_FACTOR);
    }

    public long getPeriod() {
        return period;
    }

    public long getOffset() {
        return offset;
    }
}
