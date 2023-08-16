package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class VerificationCode {

    private static final Pattern POSITIVE_REGEX = Pattern.compile("^\\d+$");
    private static final int CODE_LENGTH = 6;

    @Column(name = "code")
    private String value;

    protected VerificationCode() {
    }

    public VerificationCode(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNull(value);
        validateLength(value);
        validatePositive(value);
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateLength(String value) {
        if (value.length() != CODE_LENGTH) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void validatePositive(String value) {
        if (!POSITIVE_REGEX.matcher(value).matches()) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String getValue() {
        return value;
    }
}
