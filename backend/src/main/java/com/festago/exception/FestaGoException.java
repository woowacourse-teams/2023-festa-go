package com.festago.exception;

public abstract class FestaGoException extends RuntimeException {

    private final ErrorCode errorCode;

    protected FestaGoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
