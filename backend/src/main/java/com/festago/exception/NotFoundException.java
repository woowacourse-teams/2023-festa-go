package com.festago.exception;

public class NotFoundException extends FestaGoException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
