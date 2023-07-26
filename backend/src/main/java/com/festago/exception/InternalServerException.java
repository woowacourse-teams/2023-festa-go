package com.festago.exception;

public class InternalServerException extends FestaGoException {

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
