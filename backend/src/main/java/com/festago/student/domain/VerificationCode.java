package com.festago.student.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class VerificationCode {

    public static final int LENGTH = 6;
    private static final Pattern POSITIVE_REGEX = Pattern.compile("^\\d+$");
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
        if (value.length() != LENGTH) {
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
