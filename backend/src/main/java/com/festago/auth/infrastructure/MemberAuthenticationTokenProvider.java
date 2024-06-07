package com.festago.auth.infrastructure;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.MemberAuthentication;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.infrastructure.jwt.TokenProviderTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationTokenProvider {

    private static final long EXPIRATION_MINUTES = 60L * 6L;

    private final TokenProviderTemplate tokenProviderTemplate;

    public TokenResponse provide(MemberAuthentication memberAuthentication) {
        return tokenProviderTemplate.provide(EXPIRATION_MINUTES,
            jwtBuilder -> jwtBuilder
                .subject(memberAuthentication.getId().toString())
                .audience().add(Role.MEMBER.name()).and()
        );
    }
}
