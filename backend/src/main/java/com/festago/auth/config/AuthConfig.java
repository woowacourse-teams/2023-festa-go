package com.festago.auth.config;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.application.AuthProvider;
import com.festago.auth.application.OAuth2Client;
import com.festago.auth.application.OAuth2Clients;
import com.festago.auth.infrastructure.JwtAuthExtractor;
import com.festago.auth.infrastructure.JwtAuthProvider;
import java.time.Clock;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    private static final long EXPIRATION_MINUTES = 360;

    private final String secretKey;

    public AuthConfig(@Value("${festago.auth-secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public OAuth2Clients oAuth2Clients(List<OAuth2Client> oAuth2Clients) {
        return OAuth2Clients.builder()
            .addAll(oAuth2Clients)
            .build();
    }

    @Bean
    public AuthProvider authProvider(Clock clock) {
        return new JwtAuthProvider(secretKey, EXPIRATION_MINUTES, clock);
    }

    @Bean
    public AuthExtractor authExtractor(Clock clock) {
        return new JwtAuthExtractor(secretKey, clock);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
