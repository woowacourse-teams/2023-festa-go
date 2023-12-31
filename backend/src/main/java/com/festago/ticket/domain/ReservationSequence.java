package com.festago.ticket.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;

public class ReservationSequence {

    private static final int MOST_FAST_SEQUENCE = 1;
    private final int value;

    public ReservationSequence(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int sequence) {
        if (sequence < MOST_FAST_SEQUENCE) {
            throw new InternalServerException(ErrorCode.TICKET_SEQUENCE_DATA_ERROR);
        }
    }

    public int getValue() {
        return value;
    }
}
