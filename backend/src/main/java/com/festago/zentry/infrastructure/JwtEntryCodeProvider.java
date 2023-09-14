package com.festago.zentry.infrastructure;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import com.festago.zentry.application.EntryCodeProvider;
import com.festago.zentry.domain.EntryCodePayload;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtEntryCodeProvider implements EntryCodeProvider {

    private static final String MEMBER_TICKET_ID_KEY = "ticketId";
    private static final String ENTRY_STATE_KEY = "state";

    private final SecretKey key;

    public JwtEntryCodeProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String provide(EntryCodePayload entryCodePayload, Date expiredAt) {
        validate(expiredAt);

        return Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, entryCodePayload.getMemberTicketId())
            .claim(ENTRY_STATE_KEY, entryCodePayload.getEntryState().getIndex())
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private void validate(Date expiredAt) {
        if (expiredAt.before(new Date())) {
            throw new InternalServerException(ErrorCode.INVALID_ENTRY_CODE_EXPIRATION_TIME);
        }
    }
}
