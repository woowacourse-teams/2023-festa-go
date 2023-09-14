package com.festago.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.exception.BadRequestException;
import com.festago.exception.InternalServerException;
import com.festago.zentry.domain.EntryCodePayload;
import com.festago.zentry.infrastructure.JwtEntryCodeExtractor;
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
    void JWT_토큰의_형식이_아니면_예외() {
        // given
        String code = "Hello World";

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("올바르지 않은 입장코드입니다.");
    }

    @Test
    void 토큰이_null_이면_예외() {
        String code = null;

        // when & then
        assertThatThrownBy(() -> jwtEntryCodeExtractor.extract(code))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("올바르지 않은 입장코드입니다.");
    }

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
            .isInstanceOf(BadRequestException.class)
            .hasMessage("만료된 입장 코드입니다.");
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
            .isInstanceOf(BadRequestException.class)
            .hasMessage("올바르지 않은 입장코드입니다.");
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
            .isInstanceOf(InternalServerException.class)
            .hasMessage("유효하지 않은 입장코드 payload 입니다.");
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
            .isInstanceOf(InternalServerException.class)
            .hasMessage("올바르지 않은 입장상태 인덱스입니다.");
    }

    @Test
    void 토큰_추출_성공() {
        // given
        Long memberTicketId = 1L;
        Integer entryStateIndex = 1;
        String code = Jwts.builder()
            .claim(MEMBER_TICKET_ID_KEY, memberTicketId)
            .claim(ENTRY_STATE_KEY, entryStateIndex)
            .setExpiration(new Date(new Date().getTime() + 10000))
            .signWith(KEY, SignatureAlgorithm.HS256)
            .compact();

        // when
        EntryCodePayload payload = jwtEntryCodeExtractor.extract(code);

        // then
        assertSoftly(softly -> {
            softly.assertThat(payload.getMemberTicketId()).isEqualTo(memberTicketId);
            softly.assertThat(payload.getEntryState().getIndex()).isEqualTo(entryStateIndex);
        });
    }
}
