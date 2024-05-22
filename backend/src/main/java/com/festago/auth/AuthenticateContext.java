package com.festago.auth;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticateContext {

    private Authentication authentication = AnonymousAuthentication.getInstance();

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Long getId() {
        return authentication.getId();
    }

    public Role getRole() {
        return authentication.getRole();
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
