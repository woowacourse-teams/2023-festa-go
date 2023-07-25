package com.festago.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.domain.EntryCodePayload;
import com.festago.domain.EntryCodeProvider;
import com.festago.domain.MemberTicket;
import com.festago.support.MemberTicketFixture;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEntryCodeProviderTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";

    EntryCodeProvider entryCodeProvider = new JwtEntryCodeProvider(SECRET_KEY);

    @Test
    void expiredAt이_과거이면_예외() {
        // given
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() - 1000L);
        MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();
        EntryCodePayload payload = EntryCodePayload.from(memberTicket);

        // when & then
        assertThatThrownBy(() -> entryCodeProvider.provide(payload, expiredAt))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void memberTicketId가_null이면_예외() {
        // given
        MemberTicket memberTicket = MemberTicketFixture.memberTicket()
            .id(null)
            .build();
        EntryCodePayload payload = EntryCodePayload.from(memberTicket);

        // when & then
        assertThatThrownBy(() -> entryCodeProvider.provide(payload, new Date()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void JWT_토큰을_생성() {
        // given
        long period = 30000;
        MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + period);

        // when
        String code = entryCodeProvider.provide(EntryCodePayload.from(memberTicket), expiredAt);

        // then
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build()
            .parseClaimsJws(code)
            .getBody();

        Long actualMemberTicketId = (long) ((int) claims.get("ticketId"));
        Date actualExpiredAt = claims.getExpiration();

        assertSoftly(softAssertions -> {
            assertThat(actualExpiredAt.getTime() - now.getTime() / 1000 * 1000)
                .isEqualTo(period);
            assertThat(actualMemberTicketId).isEqualTo(memberTicket.getId());
        });
    }
}
