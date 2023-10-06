package com.festago.ticketing.domain;

import com.festago.common.exception.ValidException;
import com.festago.common.util.Validator;

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
        Validator.notNull(index, "entryState의 인덱스");
        return switch (index) {
            case 0 -> BEFORE_ENTRY;
            case 1 -> AFTER_ENTRY;
            case 2 -> AWAY;
            default -> throw new ValidException("entryState의 인덱스가 올바르지 않습니다. index: " + index);
        };
    }

    public int getIndex() {
        return index;
    }
}
