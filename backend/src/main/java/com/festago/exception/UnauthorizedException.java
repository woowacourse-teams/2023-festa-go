package com.festago.exception;

public class UnauthorizedException extends FestaGoException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
