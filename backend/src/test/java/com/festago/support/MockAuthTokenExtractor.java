package com.festago.support;

import com.festago.auth.AuthenticateContext;
import com.festago.auth.application.AuthTokenExtractor;
import com.festago.auth.domain.AuthPayload;

public class MockAuthTokenExtractor implements AuthTokenExtractor {

    private final AuthenticateContext authenticateContext;

    public MockAuthTokenExtractor(AuthenticateContext authenticateContext) {
        this.authenticateContext = authenticateContext;
    }

    @Override
    public AuthPayload extract(String token) {
        return new AuthPayload(authenticateContext.getId(), authenticateContext.getRole());
    }
}
