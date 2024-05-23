package com.festago.entry.infrastructure;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.entry.application.EntryCodeExtractor;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.ticketing.domain.EntryState;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtEntryCodeExtractor implements EntryCodeExtractor {

    private static final String MEMBER_TICKET_ID_KEY = "ticketId";
    private static final String ENTRY_STATE_KEY = "state";

    private final JwtParser jwtParser;

    public JwtEntryCodeExtractor(String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser()
            .verifyWith(key)
            .build();
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
            return jwtParser.parseSignedClaims(code)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.EXPIRED_ENTRY_CODE);
        } catch (SignatureException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException e) {
            throw new BadRequestException(ErrorCode.INVALID_ENTRY_CODE);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.");
            throw e;
        }
    }
}
