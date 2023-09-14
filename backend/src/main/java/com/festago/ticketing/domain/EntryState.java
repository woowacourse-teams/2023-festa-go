package com.festago.ticketing.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;

public enum EntryState {
    BEFORE_ENTRY(0),
    AFTER_ENTRY(1),
    AWAY(2),
    ;

    private final int index;

    EntryState(int index) {
        this.index = index;
    }

    public static EntryState from(Integer index) {
        validate(index);
        return switch (index) {
            case 0 -> BEFORE_ENTRY;
            case 1 -> AFTER_ENTRY;
            case 2 -> AWAY;
            default -> throw new InternalServerException(ErrorCode.INVALID_ENTRY_STATE_INDEX);
        };
    }

    private static void validate(Integer index) {
        if (index == null) {
            throw new InternalServerException(ErrorCode.INVALID_ENTRY_STATE_INDEX);
        }
    }

    public int getIndex() {
        return index;
    }
}
