package com.festago.auth.config;

import com.festago.auth.domain.AuthProvider;
import com.festago.auth.infrastructure.JwtAuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    private static final long EXPIRATION_MINUTES = 360;

    private final String secretKey;

    public AuthConfig(@Value("${festago.auth-secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public AuthProvider jwtAuthProvider() {
        return new JwtAuthProvider(secretKey, EXPIRATION_MINUTES);
    }
}
