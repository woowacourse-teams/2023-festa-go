package com.festago.common.exception;

public class TooManyRequestException extends FestaGoException {

    public TooManyRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
