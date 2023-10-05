package com.festago.common.exception;

public class DomainValidException extends FestaGoException {

    private final String message;

    protected DomainValidException(String message) {
        super(ErrorCode.DOMAIN_VALID_EXCEPTION);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
