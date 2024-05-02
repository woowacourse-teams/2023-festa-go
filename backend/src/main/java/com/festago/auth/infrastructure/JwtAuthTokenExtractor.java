package com.festago.auth.infrastructure;

import com.festago.auth.application.AuthTokenExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthTokenExtractor implements AuthTokenExtractor {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_ID_KEY = "role";

    private final JwtParser jwtParser;

    public JwtAuthTokenExtractor(String secretKey, Clock clock) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser()
            .clock(() -> Date.from(clock.instant()))
            .verifyWith(key)
            .build();
    }

    @Override
    public AuthPayload extract(String token) {
        Claims claims = getClaims(token);
        Long memberId = claims.get(MEMBER_ID_KEY, Long.class);
        String role = claims.get(ROLE_ID_KEY, String.class);
        return new AuthPayload(memberId, Role.from(role));
    }

    private Claims getClaims(String code) {
        try {
            return jwtParser.parseSignedClaims(code)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_AUTH_TOKEN);
        } catch (SignatureException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.");
            throw e;
        }
    }
}
