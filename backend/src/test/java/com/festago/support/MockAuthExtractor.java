package com.festago.support;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.presentation.AuthenticateContext;

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
