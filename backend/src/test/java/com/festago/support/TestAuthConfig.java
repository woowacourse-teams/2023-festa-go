package com.festago.support;

import com.festago.auth.domain.AuthenticateContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAuthConfig {

    @Bean
    public AuthenticateContext authenticateContext() {
        return new AuthenticateContext();
    }

    @Bean
    public MockAuthenticationTokenExtractor mockAuthenticationTokenExtractor(AuthenticateContext authenticateContext) {
        return new MockAuthenticationTokenExtractor(authenticateContext);
    }
}
