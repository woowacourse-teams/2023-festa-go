package com.festago.auth.infrastructure.jwt;

import static com.festago.common.exception.ErrorCode.EXPIRED_AUTH_TOKEN;
import static com.festago.common.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.auth.domain.Role;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtTokenParserTest {

    private static final String KEY = "1231231231231231223131231231231231231212312312";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));

    JwtTokenParser jwtTokenParser;

    @BeforeEach
    void setUp() {
        jwtTokenParser = new JwtTokenParser(
            KEY,
            Clock.systemDefaultZone()
        );
    }

    @Test
    void JWT_토큰의_형식이_아니면_예외() {
        // given
        String token = "Hello World";

        // when & then
        assertThatThrownBy(() -> jwtTokenParser.getClaims(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void 기간이_만료된_토큰이면_예외() {
        //given
        String token = Jwts.builder()
            .audience().add(Role.MEMBER.name()).and()
            .expiration(new Date(new Date().getTime() - 1000))
            .signWith(SECRET_KEY)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenParser.getClaims(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(EXPIRED_AUTH_TOKEN.getMessage());
    }

    @Test
    void 키값이_유효하지_않으면_예외() {
        // given
        Key otherKey = Keys.hmacShaKeyFor(("a" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
            .audience().add(Role.MEMBER.name()).and()
            .expiration(new Date(new Date().getTime() + 10000))
            .signWith(otherKey)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenParser.getClaims(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void token이_null이면_예외() {
        // when & then
        assertThatThrownBy(() -> jwtTokenParser.getClaims(null))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void 토큰_추출_성공() {
        // given
        String token = Jwts.builder()
            .audience().add(Role.MEMBER.name()).and()
            .expiration(new Date(new Date().getTime() + 10000))
            .signWith(SECRET_KEY)
            .compact();

        // when
        Claims claims = jwtTokenParser.getClaims(token);

        // then
        assertThat(claims.getAudience()).containsOnly(Role.MEMBER.name());
    }
}
