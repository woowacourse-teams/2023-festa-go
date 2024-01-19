package com.festago.common.converter;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.SchoolRegion;
import org.springframework.core.convert.converter.Converter;

public class SchoolRegionConverter implements Converter<String, SchoolRegion> {

    @Override
    public SchoolRegion convert(String value) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        return SchoolRegion.valueOf(value);
    }
}
