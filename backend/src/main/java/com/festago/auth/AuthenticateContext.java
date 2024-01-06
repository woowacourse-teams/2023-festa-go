package com.festago.auth;

import com.festago.auth.domain.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticateContext {

    private Long id;
    private Role role = Role.ANONYMOUS;

    public void setAuthenticate(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
