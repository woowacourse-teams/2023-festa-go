package com.festago.auth.infrastructure;

import com.festago.auth.application.AuthProvider;
import com.festago.auth.domain.AuthPayload;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtAuthProvider implements AuthProvider {

    private static final int SECOND_FACTOR = 60;
    private static final int MILLISECOND_FACTOR = 1000;
    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_ID_KEY = "role";

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtAuthProvider(String secretKey, long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String provide(AuthPayload authPayload) {
        Date now = new Date();
        return Jwts.builder()
            .claim(MEMBER_ID_KEY, authPayload.getMemberId())
            .claim(ROLE_ID_KEY, authPayload.getRole())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + expirationMinutes * SECOND_FACTOR * MILLISECOND_FACTOR))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}
