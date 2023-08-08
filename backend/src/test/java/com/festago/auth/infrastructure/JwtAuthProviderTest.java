package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.AuthPayload;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtAuthProviderTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";
    JwtAuthProvider jwtAuthProvider = new JwtAuthProvider(SECRET_KEY, 360);

    @Test
    void 토큰_생성_성공() {
        // given
        AuthPayload authPayload = new AuthPayload(1L);
        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build();

        // when
        String token = jwtAuthProvider.provide(authPayload);

        // then
        assertThat(parser.isSigned(token))
            .isTrue();
    }
}
