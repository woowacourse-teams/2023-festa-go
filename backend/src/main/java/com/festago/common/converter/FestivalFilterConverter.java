package com.festago.common.converter;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.repository.FestivalFilter;
import org.springframework.core.convert.converter.Converter;

public class FestivalFilterConverter implements Converter<String, FestivalFilter> {

    @Override
    public FestivalFilter convert(String value) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        return FestivalFilter.valueOf(value);
    }
}
