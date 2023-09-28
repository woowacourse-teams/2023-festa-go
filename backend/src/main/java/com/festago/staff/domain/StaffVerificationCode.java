package com.festago.staff.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class StaffVerificationCode {

    public static final int RANDOM_CODE_LENGTH = 4;

    @NotNull
    @Column(name = "code")
    private String value;

    protected StaffVerificationCode() {
    }

    public StaffVerificationCode(String value) {
        checkNotNull(value);
        this.value = value;
    }

    private void checkNotNull(String value) {
        if (value == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String getValue() {
        return value;
    }
}
