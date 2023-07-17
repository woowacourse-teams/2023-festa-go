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

    private static final String MEMBER_TICKET_ID_KEY = "ticketId";

    private final SecretKey key;

    public JwtEntryCodeProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private static void validate(Long memberTicketId, Date expiredAt) {
        if (Objects.isNull(memberTicketId)) {
            throw new IllegalArgumentException(); // TODO
        }
        if (expiredAt.before(new Date())) {
            throw new IllegalArgumentException(); // TODO
        }
    }

    @Override
    public String provide(MemberTicket memberTicket, Date expiredAt) {
        validate(memberTicket.getId(), expiredAt);
        Long memberTicketId = memberTicket.getId();

        return Jwts.builder()
                .claim(MEMBER_TICKET_ID_KEY, memberTicketId)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
