package com.festago.auth.infrastructure.openid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AppleOpenIdClientTest {

    AppleOpenIdClient appleOpenIdClient;

    AppleOpenIdPublicKeyLocator keyLocator;

    Clock clock;

    Key key = Keys.hmacShaKeyFor("key".repeat(15).getBytes(StandardCharsets.UTF_8));

    @BeforeEach
    void setUp() {
        keyLocator = mock();
        clock = spy(Clock.systemDefaultZone());
        appleOpenIdClient = new AppleOpenIdClient(
            "appleClientId",
            keyLocator,
            new NoopOpenIdNonceValidator(),
            clock
        );
    }

    @Test
    void audience가_올바르지_않으면_예외() {
        // given
        given(keyLocator.locate(any()))
            .willReturn(key);
        String idToken = Jwts.builder()
            .audience().add("wrong")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when & then
        assertThatThrownBy(() -> appleOpenIdClient.getUserInfo(idToken))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(ErrorCode.OPEN_ID_INVALID_TOKEN.getMessage());
    }

    @Test
    void issuer가_올바르지_않으면_예외() {
        // given
        given(keyLocator.locate(any()))
            .willReturn(key);
        String idToken = Jwts.builder()
            .audience().add("client-id")
            .and()
            .issuer("wrong")
            .signWith(key)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when & then
        assertThatThrownBy(() -> appleOpenIdClient.getUserInfo(idToken))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(ErrorCode.OPEN_ID_INVALID_TOKEN.getMessage());
    }

    @Test
    void 토큰이_만료되면_예외() {
        // given
        given(keyLocator.locate(any()))
            .willReturn(key);
        Date yesterday = Date.from(clock.instant().minus(1, ChronoUnit.DAYS));
        String idToken = Jwts.builder()
            .audience().add("client-id")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .expiration(yesterday)
            .compact();

        // when & then
        assertThatThrownBy(() -> appleOpenIdClient.getUserInfo(idToken))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(ErrorCode.OPEN_ID_INVALID_TOKEN.getMessage());
    }

    @Test
    void 토큰에_서명된_키가_파싱할때_키와_일치하지_않으면_예외() {
        // given
        Key otherKey = Keys.hmacShaKeyFor("otherKey".repeat(10).getBytes(StandardCharsets.UTF_8));
        given(keyLocator.locate(any()))
            .willReturn(otherKey);
        String idToken = Jwts.builder()
            .audience().add("client-id")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when & then
        assertThatThrownBy(() -> appleOpenIdClient.getUserInfo(idToken))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(ErrorCode.OPEN_ID_INVALID_TOKEN.getMessage());
    }

    @Test
    void 파싱할때_키가_null이면_예외() {
        // given
        given(keyLocator.locate(any()))
            .willReturn(null);
        String idToken = Jwts.builder()
            .audience().add("client-id")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when & then
        assertThatThrownBy(() -> appleOpenIdClient.getUserInfo(idToken))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(ErrorCode.OPEN_ID_INVALID_TOKEN.getMessage());
    }

    @Test
    void audience_issuer가_올바르면_성공() {
        // given
        String socialId = "12345";
        given(keyLocator.locate(any()))
            .willReturn(key);
        String idToken = Jwts.builder()
            .audience().add("appleClientId")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .subject(socialId)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when
        var expect = appleOpenIdClient.getUserInfo(idToken);

        // then
        assertThat(expect.socialId()).isEqualTo(socialId);
    }

    @Test
    void audience_값은_apple_client_id_와_같으면_성공() {
        // given
        String socialId = "12345";
        given(keyLocator.locate(any()))
            .willReturn(key);
        String idToken = Jwts.builder()
            .audience().add("appleClientId")
            .and()
            .issuer("https://appleid.apple.com")
            .signWith(key)
            .subject(socialId)
            .expiration(Date.from(clock.instant().plus(1, ChronoUnit.DAYS)))
            .compact();

        // when
        var expect = appleOpenIdClient.getUserInfo(idToken);

        // then
        assertThat(expect.socialId()).isEqualTo(socialId);
    }


}
