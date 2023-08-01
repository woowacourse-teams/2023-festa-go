package com.festago.exception;

import org.springframework.core.NestedRuntimeException;

public abstract class FestaGoException extends NestedRuntimeException {

    private final ErrorCode errorCode;

    protected FestaGoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
