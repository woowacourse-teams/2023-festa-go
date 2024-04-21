package com.festago.auth.infrastructure;

import com.festago.auth.application.AuthProvider;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.v1.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;

public class JwtAuthProvider implements AuthProvider {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_ID_KEY = "role";

    private final SecretKey key;
    private final long expirationMinutes;
    private final Clock clock;

    public JwtAuthProvider(String secretKey, long expirationMinutes, Clock clock) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
        this.clock = clock;
    }

    @Override
    public TokenResponse provide(AuthPayload authPayload) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime expiredAt = now.plusMinutes(expirationMinutes);
        String accessToken = Jwts.builder()
            .claim(MEMBER_ID_KEY, authPayload.getMemberId())
            .claim(ROLE_ID_KEY, authPayload.getRole())
            .setIssuedAt(Timestamp.valueOf(now))
            .setExpiration(Timestamp.valueOf(expiredAt))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
        return new TokenResponse(accessToken, expiredAt);
    }
}
