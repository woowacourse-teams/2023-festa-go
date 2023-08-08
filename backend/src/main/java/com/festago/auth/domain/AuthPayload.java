package com.festago.auth.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;

public class AuthPayload {

    private final Long memberId;

    public AuthPayload(Long memberId) {
        validate(memberId);
        this.memberId = memberId;
    }

    private void validate(Long memberId) {
        if (memberId == null) {
            throw new InternalServerException(ErrorCode.INVALID_AUTH_TOKEN_PAYLOAD);
        }
    }

    public Long getMemberId() {
        return memberId;
    }
}
