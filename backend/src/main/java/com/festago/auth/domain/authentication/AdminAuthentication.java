package com.festago.auth.domain.authentication;

import com.festago.auth.domain.Role;
import com.festago.common.exception.UnexpectedException;

public class AdminAuthentication implements Authentication {

    private final Long id;

    public AdminAuthentication(Long id) {
        if (id == null) {
            throw new UnexpectedException("id는 null이 될 수 없습니다.");
        }
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
