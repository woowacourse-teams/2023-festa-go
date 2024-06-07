package com.festago.auth.infrastructure;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.infrastructure.jwt.TokenProviderTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationTokenProvider {

    private static final long EXPIRATION_MINUTES = 60L * 24L;

    private final TokenProviderTemplate tokenProviderTemplate;

    public TokenResponse provide(AdminAuthentication adminAuthentication) {
        return tokenProviderTemplate.provide(EXPIRATION_MINUTES,
            jwtBuilder -> jwtBuilder
                .subject(adminAuthentication.getId().toString())
                .audience().add(Role.ADMIN.name()).and()
        );
    }
}
