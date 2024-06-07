package com.festago.support;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.authentication.Authentication;

public class MockAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final AuthenticateContext authenticateContext;

    public MockAuthenticationTokenExtractor(AuthenticateContext authenticateContext) {
        this.authenticateContext = authenticateContext;
    }

    @Override
    public Authentication extract(String token) {
        return authenticateContext.getAuthentication();
    }
}
