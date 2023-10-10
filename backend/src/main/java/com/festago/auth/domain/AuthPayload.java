package com.festago.auth.domain;

import com.festago.common.exception.UnexpectedException;

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
            throw new UnexpectedException("role은 null이 될 수 없습니다.");
        }
    }

    public Long getMemberId() {
        return memberId;
    }

    public Role getRole() {
        return role;
    }
}
