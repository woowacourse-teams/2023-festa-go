package com.festago.auth.infrastructure.jwt;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.ADMIN.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long adminId = Long.parseLong(claims.getSubject());
        return new AdminAuthentication(adminId);
    }
}
