package com.festago.auth.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;

public class AuthPayload {

    private final Long memberId;
    private final Role role;

    public AuthPayload(Long memberId, Role role) {
        validate(role);
        this.memberId = memberId;
        this.role = role;
    }

    private void validate(Role role) {
        if (role == null) {
            throw new InternalServerException(ErrorCode.INVALID_AUTH_TOKEN_PAYLOAD);
        }
    }

    public Long getMemberId() {
        return memberId;
    }

    public Role getRole() {
        return role;
    }
}
