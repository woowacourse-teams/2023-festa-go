package com.festago.common.exception;

public class ConflictException extends FestaGoException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
