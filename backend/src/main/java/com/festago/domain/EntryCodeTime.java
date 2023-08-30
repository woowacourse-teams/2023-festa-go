package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.util.Date;

public class EntryCodeTime {

    private static final long DEFAULT_PERIOD = 30;
    private static final long DEFAULT_OFFSET = 10;
    private static final int MILLISECOND_FACTOR = 1000;
    private static final int MINIMUM_PERIOD = 0;
    private static final int MINIMUM_OFFSET = 0;

    private final long period;
    private final long offset;

    public EntryCodeTime() {
        this(DEFAULT_PERIOD, DEFAULT_OFFSET);
    }

    public EntryCodeTime(long period, long offset) {
        validate(period, offset);
        this.period = period;
        this.offset = offset;
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
