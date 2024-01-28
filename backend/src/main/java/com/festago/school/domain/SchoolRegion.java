package com.festago.school.domain;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;

public enum SchoolRegion {
    서울,
    부산,
    대구,
    기타;

    public static SchoolRegion from(String regionName) {
        try {
            return SchoolRegion.valueOf(regionName);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ErrorCode.INVALID_REGION);
        }
    }
}
