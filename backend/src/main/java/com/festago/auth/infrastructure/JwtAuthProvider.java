package com.festago.auth.infrastructure;

import com.festago.auth.domain.AuthProvider;
import com.festago.domain.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtAuthProvider implements AuthProvider {

    private static final int SECOND_FACTOR = 60;
    private static final int MILLISECOND_FACTOR = 1000;

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtAuthProvider(String secretKey, long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String provide(Member member) {
        Date now = new Date();
        return Jwts.builder()
            .claim("memberId", member.getId())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + expirationMinutes * SECOND_FACTOR * MILLISECOND_FACTOR))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}
