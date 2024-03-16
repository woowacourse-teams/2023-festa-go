package com.festago.common.exception.dto;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.FestaGoException;
import com.festago.common.exception.ValidException;

public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {

    public static ErrorResponse from(FestaGoException festaGoException) {
        return ErrorResponse.from(festaGoException.getErrorCode());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }

    public static ErrorResponse from(ValidException e) {
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }
}
