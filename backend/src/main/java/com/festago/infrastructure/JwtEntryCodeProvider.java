package com.festago.infrastructure;

import com.festago.domain.EntryCodeProvider;
import com.festago.domain.MemberTicket;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;

public class JwtEntryCodeProvider implements EntryCodeProvider {

    private static final int MILLISECOND_FACTOR = 1000;
    private static final String MEMBER_TICKET_ID_KEY = "memberTicketId";
    private static final int MINIMUM_PERIOD = 0;

    private final SecretKey key;

    public JwtEntryCodeProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String provide(MemberTicket memberTicket, long period) {
        Long memberTicketId = memberTicket.getId();
        validate(memberTicketId, period);

        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + period * MILLISECOND_FACTOR);

        return Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, memberTicketId)
            .setIssuedAt(now)
            .setExpiration(expirationTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private static void validate(Long memberTicketId, long period) {
        if (Objects.isNull(memberTicketId)) {
            throw new IllegalArgumentException(); // TODO
        }
        if (period <= MINIMUM_PERIOD) {
            throw new IllegalArgumentException(); // TODO
        }
    }
}
