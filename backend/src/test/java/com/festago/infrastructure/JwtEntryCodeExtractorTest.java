package com.festago.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.domain.EntryCodePayload;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEntryCodeExtractorTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";
    private static final String MEMBER_TICKET_ID_KEY = "ticketId";
    private static final String ENTRY_STATE_KEY = "state";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    JwtEntryCodeExtractor jwtEntryCodeExtractor = new JwtEntryCodeExtractor(SECRET_KEY);

    @Test
    void 기간이_만료된_토큰이면_예외() {
        //given
        String code = Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, 1L)
            .claim(ENTRY_STATE_KEY, 1)
            .setExpiration(new Date(new Date().getTime() - 1000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 키값이_유효하지_않으면_예외() {
        // given
        Key otherKey = Keys.hmacShaKeyFor(("a" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

        String code = Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, 1L)
            .claim(ENTRY_STATE_KEY, 1)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(otherKey, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void memberTicketId_필드가_없으면_예외() {
        // given
        String code = Jwts.builder()
            .claim(ENTRY_STATE_KEY, 1)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void entryStateIndex_필드가_없으면_예외() {
        // given
        String code = Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, 1L)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 토큰_추출_성공() {
        // given
        Long memberTicketId = 1L;
        int entryStateIndex = 1;
        String code = Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, memberTicketId)
            .claim(ENTRY_STATE_KEY, entryStateIndex)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when
        EntryCodePayload payload = jwtEntryCodeExtractor.extract(code);

        // then
        assertSoftly(softAssertions -> {
            assertThat(payload.getMemberTicketId()).isEqualTo(memberTicketId);
            assertThat(payload.getEntryState().getIndex()).isEqualTo(entryStateIndex);
        });
    }
}
