package com.festago.auth.infrastructure;

import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.jwt.AdminAuthenticationClaimsExtractor;
import com.festago.auth.infrastructure.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final AdminAuthenticationClaimsExtractor adminAuthenticationClaimsExtractor;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        return adminAuthenticationClaimsExtractor.extract(claims);
    }
}
