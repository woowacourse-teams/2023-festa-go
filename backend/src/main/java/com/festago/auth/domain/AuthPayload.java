package com.festago.auth.domain;

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
            throw new IllegalArgumentException("role은 null이 될 수 없습니다.");
        }
    }

    public Long getMemberId() {
        return memberId;
    }

    public Role getRole() {
        return role;
    }
}
