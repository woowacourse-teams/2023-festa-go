package com.festago.common.exception;

public class ValidException extends FestaGoException {

    public ValidException(String message) {
        super(ErrorCode.DOMAIN_VALID_EXCEPTION, message);
    }
}
