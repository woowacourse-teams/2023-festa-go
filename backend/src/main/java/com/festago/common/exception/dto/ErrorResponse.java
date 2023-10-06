package com.festago.common.exception.dto;

import com.festago.common.exception.DomainValidException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.FestaGoException;
import java.util.List;
import org.springframework.validation.FieldError;
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

    public static ErrorResponse from(DomainValidException e) {
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }

    public static ErrorResponse from(ErrorCode errorCode, MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            return new ErrorResponse(errorCode, errorCode.getMessage());
        }
        if (e.getMessage().startsWith(NOT_CUSTOM_EXCEPTION)) {
            return new ErrorResponse(errorCode, fieldErrors.get(0).getDefaultMessage());
        }
        return new ErrorResponse(errorCode, e.getMessage());
    }
}
