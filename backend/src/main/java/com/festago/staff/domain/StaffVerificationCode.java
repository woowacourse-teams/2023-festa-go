package com.festago.staff.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StaffVerificationCode {

    public static final int RANDOM_CODE_LENGTH = 4;

    @Column(name = "code")
    private String value;

    protected StaffVerificationCode() {
    }

    public StaffVerificationCode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
