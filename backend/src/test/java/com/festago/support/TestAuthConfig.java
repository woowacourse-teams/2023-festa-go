package com.festago.support;

import com.festago.auth.AuthenticateContext;
import com.festago.auth.application.AuthTokenExtractor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAuthConfig {

    @Bean
    public AuthenticateContext authenticateContext() {
        return new AuthenticateContext();
    }

    @Bean
    public AuthTokenExtractor authExtractor(AuthenticateContext authenticateContext) {
        return Mockito.spy(new MockAuthTokenExtractor(authenticateContext));
    }
}
