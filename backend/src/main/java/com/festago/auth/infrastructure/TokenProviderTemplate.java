package com.festago.auth.infrastructure;

import com.festago.auth.dto.v1.TokenResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.UnaryOperator;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProviderTemplate {

    private final SecretKey secretKey;
    private final Clock clock;

    public TokenProviderTemplate(
        @Value("${festago.auth-secret-key}") String secretKey,
        Clock clock
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.clock = clock;
    }

    public TokenResponse provide(long expirationMinutes, UnaryOperator<JwtBuilder> template) {
        Instant now = clock.instant();
        Instant expiredAt = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        JwtBuilder builder = Jwts.builder()
            .expiration(Date.from(expiredAt))
            .issuedAt(Date.from(now))
            .signWith(secretKey);
        template.apply(builder);
        String accessToken = builder.compact();
        return new TokenResponse(accessToken, LocalDateTime.ofInstant(expiredAt, clock.getZone()));
    }
}
