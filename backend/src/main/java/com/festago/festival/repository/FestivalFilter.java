package com.festago.festival.repository;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;

public enum FestivalFilter {
    PROGRESS,
    PLANNED,
    END;

    public static FestivalFilter from(String filterName) {
        return switch (filterName.toUpperCase()) {
            case "PROGRESS" -> PROGRESS;
            case "PLANNED" -> PLANNED;
            case "END" -> END;
            default -> throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_FILTER);
        };
    }
}
