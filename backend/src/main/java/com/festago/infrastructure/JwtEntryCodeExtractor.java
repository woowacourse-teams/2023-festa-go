package com.festago.infrastructure;

import com.festago.domain.EntryCodeExtractor;
import com.festago.domain.EntryCodePayload;
import com.festago.domain.EntryState;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class JwtEntryCodeExtractor implements EntryCodeExtractor {

    private static final String MEMBER_TICKET_ID_KEY = "ticketId";
    private static final String ENTRY_STATE_KEY = "state";

    private final SecretKey key;

    public JwtEntryCodeExtractor(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public EntryCodePayload extract(String code) {
        Claims claims = getClaims(code);
        Long memberTicketId = claims.get(MEMBER_TICKET_ID_KEY, Long.class);
        EntryState entryState = EntryState.from(claims.get(ENTRY_STATE_KEY, Integer.class));

        return new EntryCodePayload(memberTicketId, entryState);
    }

    private Claims getClaims(String code) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(code)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException(); // TODO
        } catch (SignatureException e) {
            throw new IllegalArgumentException(); // TODO
        } catch (JwtException e) {
            throw new IllegalArgumentException(); // TODO
        }
    }
}
