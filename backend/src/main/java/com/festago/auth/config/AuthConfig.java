package com.festago.auth.config;

import com.festago.auth.application.AuthTokenExtractor;
import com.festago.auth.application.AuthTokenProvider;
import com.festago.auth.application.OAuth2Client;
import com.festago.auth.application.OAuth2Clients;
import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.OpenIdClients;
import com.festago.auth.infrastructure.JwtAuthTokenExtractor;
import com.festago.auth.infrastructure.JwtAuthTokenProvider;
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
    public OpenIdClients openIdClients(List<OpenIdClient> openIdClients) {
        return OpenIdClients.builder()
            .addAll(openIdClients)
            .build();
    }

    @Bean
    public AuthTokenProvider authProvider(Clock clock) {
        return new JwtAuthTokenProvider(secretKey, EXPIRATION_MINUTES, clock);
    }

    @Bean
    public AuthTokenExtractor authExtractor(Clock clock) {
        return new JwtAuthTokenExtractor(secretKey, clock);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
