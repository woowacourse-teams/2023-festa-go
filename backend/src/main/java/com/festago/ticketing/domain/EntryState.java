package com.festago.ticketing.domain;

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
        validateNull(index);
        return switch (index) {
            case 0 -> BEFORE_ENTRY;
            case 1 -> AFTER_ENTRY;
            case 2 -> AWAY;
            default -> throw new IllegalArgumentException("entryState의 인덱스가 올바르지 않습니다. index: " + index);
        };
    }

    private static void validateNull(Integer index) {
        if (index == null) {
            throw new IllegalArgumentException("entryState의 인덱스는 null이 될 수 없습니다.");
        }
    }

    public int getIndex() {
        return index;
    }
}
