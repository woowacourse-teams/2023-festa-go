package com.festago.auth.infrastructure;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    private static final String ADMIN_ID_KEY = "adminId";

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.ADMIN.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long adminId = claims.get(ADMIN_ID_KEY, Long.class);
        return new AdminAuthentication(adminId);
    }
}
