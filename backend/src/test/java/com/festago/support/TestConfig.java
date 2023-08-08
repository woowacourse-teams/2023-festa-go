package com.festago.support;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.infrastructure.JwtAuthExtractor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";

    @Bean
    public AuthExtractor testAuthExtractor() {
        return new JwtAuthExtractor(SECRET_KEY);
    }
}
