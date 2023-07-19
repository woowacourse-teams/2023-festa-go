package com.festago.domain;

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
            default -> throw new IllegalArgumentException();
        };
    }

    private static void validate(Integer index) {
        if (index == null) {
            throw new IllegalArgumentException();
        }
    }

    public int getIndex() {
        return index;
    }
}
