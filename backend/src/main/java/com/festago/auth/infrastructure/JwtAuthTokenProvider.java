package com.festago.auth.infrastructure;

import com.festago.auth.application.AuthTokenProvider;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.v1.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtAuthTokenProvider implements AuthTokenProvider {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_ID_KEY = "role";

    private final SecretKey key;
    private final long expirationMinutes;
    private final Clock clock;

    public JwtAuthTokenProvider(String secretKey, long expirationMinutes, Clock clock) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
        this.clock = clock;
    }

    @Override
    public TokenResponse provide(AuthPayload authPayload) {
        Instant now = clock.instant();
        Instant expiredAt = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        String accessToken = Jwts.builder()
            .claim(MEMBER_ID_KEY, authPayload.getMemberId())
            .claim(ROLE_ID_KEY, authPayload.getRole())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiredAt))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
        return new TokenResponse(accessToken, LocalDateTime.ofInstant(expiredAt, clock.getZone()));
    }
}
