package com.festago.auth.infrastructure.jwt;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.domain.authentication.MemberAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.MEMBER.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long memberId = Long.parseLong(claims.getSubject());
        return new MemberAuthentication(memberId);
    }
}
