package com.festago.auth.infrastructure;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.domain.authentication.MemberAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    private static final String MEMBER_ID_KEY = "memberId";

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.MEMBER.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long memberId = claims.get(MEMBER_ID_KEY, Long.class);
        return new MemberAuthentication(memberId);
    }
}
