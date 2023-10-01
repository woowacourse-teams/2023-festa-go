package com.festago.common.exception.dto;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.FestaGoException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {

    private static final String NOT_CUSTOM_EXCEPTION = "Validation failed";

    public static ErrorResponse from(FestaGoException festaGoException) {
        return ErrorResponse.from(festaGoException.getErrorCode());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }

    public static ErrorResponse from(ErrorCode errorCode, MethodArgumentNotValidException e) {
        if (e.getMessage().startsWith(NOT_CUSTOM_EXCEPTION)) {
            return new ErrorResponse(errorCode, errorCode.getMessage());
        }
        return new ErrorResponse(errorCode, e.getMessage());
    }
}
