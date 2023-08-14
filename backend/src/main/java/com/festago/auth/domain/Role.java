package com.festago.auth.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;

public enum Role {
    ANONYMOUS,
    MEMBER,
    ADMIN,
    ;

    public static Role from(String role) {
        try {
            return valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new InternalServerException(ErrorCode.INVALID_ROLE_NAME);
        }
    }
}
