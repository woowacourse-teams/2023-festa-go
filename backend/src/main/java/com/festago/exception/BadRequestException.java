package com.festago.exception;

public class BadRequestException extends FestaGoException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
