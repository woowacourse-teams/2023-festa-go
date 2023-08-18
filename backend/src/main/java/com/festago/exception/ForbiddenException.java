package com.festago.exception;

public class ForbiddenException extends FestaGoException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
