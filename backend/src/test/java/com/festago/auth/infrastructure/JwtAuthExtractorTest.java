package com.festago.auth.infrastructure;

import static com.festago.common.exception.ErrorCode.EXPIRED_AUTH_TOKEN;
import static com.festago.common.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.common.exception.UnauthorizedException;
import com.festago.common.exception.UnexpectedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.util.Date;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtAuthExtractorTest {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_ID_KEY = "role";
    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    JwtAuthTokenExtractor jwtAuthExtractor = new JwtAuthTokenExtractor(SECRET_KEY, Clock.systemDefaultZone());

    @Test
    void JWT_토큰의_형식이_아니면_예외() {
        // given
        String token = "Hello World";

        // when & then
        assertThatThrownBy(() -> jwtAuthExtractor.extract(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void 기간이_만료된_토큰이면_예외() {
        //given
        String token = Jwts.builder()
            .claim(MEMBER_ID_KEY, 1L)
            .setExpiration(new Date(new Date().getTime() - 1000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtAuthExtractor.extract(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(EXPIRED_AUTH_TOKEN.getMessage());
    }

    @Test
    void 키값이_유효하지_않으면_예외() {
        // given
        Key otherKey = Keys.hmacShaKeyFor(("a" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
            .claim(MEMBER_ID_KEY, 1L)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(otherKey, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtAuthExtractor.extract(token))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void role_필드가_없으면_예외() {
        // given
        String token = Jwts.builder()
            .claim(MEMBER_ID_KEY, 1)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtAuthExtractor.extract(token))
            .isInstanceOf(UnexpectedException.class)
            .hasMessage("해당하는 Role이 없습니다.");
    }

    @Test
    void token이_null이면_예외() {
        // when & then
        assertThatThrownBy(() -> jwtAuthExtractor.extract(null))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(INVALID_AUTH_TOKEN.getMessage());
    }

    @Test
    void 토큰_추출_성공() {
        // given
        Long memberId = 1L;
        String token = Jwts.builder()
            .claim(MEMBER_ID_KEY, memberId)
            .claim(ROLE_ID_KEY, Role.MEMBER)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when
        AuthPayload payload = jwtAuthExtractor.extract(token);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(payload.getMemberId()).isEqualTo(memberId);
            softly.assertThat(payload.getRole()).isEqualTo(Role.MEMBER);
        });
    }
}
