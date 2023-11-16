package com.festago.common.exception;

public class UnexpectedException extends FestaGoException {

    public UnexpectedException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}
