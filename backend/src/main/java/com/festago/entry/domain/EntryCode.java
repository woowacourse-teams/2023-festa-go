package com.festago.entry.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;

public class EntryCode {

    private static final int MINIMUM_PERIOD = 0;
    private static final int MINIMUM_OFFSET = 0;

    private final String code;
    private final long period;
    private final long offset;

    public EntryCode(String code, long period, long offset) {
        validate(period, offset);
        this.code = code;
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
