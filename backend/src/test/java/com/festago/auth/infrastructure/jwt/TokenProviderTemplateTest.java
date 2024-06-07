package com.festago.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.dto.v1.TokenResponse;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TokenProviderTemplateTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";

    TokenProviderTemplate tokenProviderTemplate;

    @BeforeEach
    void setUp() {
        tokenProviderTemplate = new TokenProviderTemplate(
            SECRET_KEY,
            Clock.systemDefaultZone()
        );
    }

    @Test
    void 토큰_생성_성공() {
        // given
        JwtParser parser = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
            .build();

        // when
        TokenResponse response = tokenProviderTemplate.provide(60, jwtBuilder -> jwtBuilder);

        // then
        assertThat(parser.isSigned(response.token()))
            .isTrue();
    }
}
