package com.festago.common.exception;

public class TooManyRequestException extends FestaGoException {

    public TooManyRequestException() {
        this(ErrorCode.TOO_FREQUENT_REQUESTS);
    }

    public TooManyRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
