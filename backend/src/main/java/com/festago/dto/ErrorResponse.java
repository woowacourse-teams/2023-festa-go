package com.festago.dto;

import com.festago.exception.ErrorCode;
import com.festago.exception.FestaGoException;

public record ErrorResponse(
    ErrorCode errorCode,
    String message) {

    public static ErrorResponse from(FestaGoException festaGoException) {
        return ErrorResponse.from(festaGoException.getErrorCode());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }
}
