package com.festago.support;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.presentation.auth.AuthenticateContext;

public class MockAuthExtractor implements AuthExtractor {

    private final AuthenticateContext authenticateContext;

    public MockAuthExtractor(AuthenticateContext authenticateContext) {
        this.authenticateContext = authenticateContext;
    }

    @Override
    public AuthPayload extract(String token) {
        return new AuthPayload(authenticateContext.getId(), authenticateContext.getRole());
    }
}
