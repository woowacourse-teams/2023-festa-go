package com.festago.auth.domain;

public class AuthPayload {

    private final Long memberId;

    public AuthPayload(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
