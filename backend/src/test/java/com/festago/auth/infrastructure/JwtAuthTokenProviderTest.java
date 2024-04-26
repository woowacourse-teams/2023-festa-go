package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.v1.TokenResponse;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtAuthTokenProviderTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";
    JwtAuthTokenProvider jwtAuthProvider = new JwtAuthTokenProvider(SECRET_KEY, 360, Clock.systemDefaultZone());

    @Test
    void 토큰_생성_성공() {
        // given
        AuthPayload authPayload = new AuthPayload(1L, Role.MEMBER);
        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build();

        // when
        TokenResponse response = jwtAuthProvider.provide(authPayload);

        // then
        assertThat(parser.isSigned(response.token()))
            .isTrue();
    }
}
