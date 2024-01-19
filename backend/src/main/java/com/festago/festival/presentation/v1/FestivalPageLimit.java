package com.festago.festival.presentation.v1;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;

public class FestivalPageLimit {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 20;

    private final Integer limit;

    public FestivalPageLimit(Integer limit) {
        validate(limit);
        this.limit = limit;
    }

    private void validate(Integer limit) {
        if (limit < MIN_PAGE_SIZE || limit > MAX_PAGE_SIZE) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
    }

    public Integer getLimit() {
        return limit;
    }
}
