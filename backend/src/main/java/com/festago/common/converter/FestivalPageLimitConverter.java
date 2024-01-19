package com.festago.common.converter;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.presentation.v1.FestivalPageLimit;
import org.springframework.core.convert.converter.Converter;

public class FestivalPageLimitConverter implements Converter<String, FestivalPageLimit> {

    @Override
    public FestivalPageLimit convert(String value) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        return new FestivalPageLimit(parse(value));
    }

    private Integer parse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
    }
}
