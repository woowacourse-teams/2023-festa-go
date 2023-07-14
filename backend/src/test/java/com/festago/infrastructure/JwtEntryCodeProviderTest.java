package com.festago.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.domain.EntryCodeProvider;
import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.Ticket;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEntryCodeProviderTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";

    EntryCodeProvider entryCodeProvider = new JwtEntryCodeProvider(SECRET_KEY);

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void period가_0이하이면_예외(long period) {
        // given
        Long memberTicketId = 1L;
        MemberTicket memberTicket = new MemberTicket(memberTicketId, new Member(1L), new Ticket(LocalDateTime.now()));

        // when & then
        assertThatThrownBy(() -> entryCodeProvider.provide(memberTicket, period))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void memberTicketId가_null이면_예외() {
        // given
        Long memberTicketId = null;
        MemberTicket memberTicket = new MemberTicket(memberTicketId, new Member(1L), new Ticket(LocalDateTime.now()));

        // when & then
        assertThatThrownBy(() -> entryCodeProvider.provide(memberTicket, 30))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void JWT_토큰을_생성() {
        // given
        long period = 30;
        Long memberTicketId = 1L;
        MemberTicket memberTicket = new MemberTicket(memberTicketId, new Member(1L), new Ticket(LocalDateTime.now()));

        // when
        String code = entryCodeProvider.provide(memberTicket, period);

        // then
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build()
            .parseClaimsJws(code);

        Long actual = (long) ((int) claims.getBody()
            .get("memberTicketId"));
        Date issuedAt = claims.getBody()
            .getIssuedAt();
        Date expiration = claims.getBody()
            .getExpiration();

        assertThat(expiration.getTime() - issuedAt.getTime())
            .isEqualTo(period * 1000);
        assertThat(actual).isEqualTo(memberTicketId);
    }
}
