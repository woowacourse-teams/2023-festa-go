package com.festago.auth.infrastructure;

import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.jwt.AuthenticationClaimsExtractor;
import com.festago.auth.infrastructure.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositeAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final List<AuthenticationClaimsExtractor> authenticationClaimsExtractors;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        for (AuthenticationClaimsExtractor claimsExtractor : authenticationClaimsExtractors) {
            Authentication authentication = claimsExtractor.extract(claims);
            if (authentication.getRole() != Role.ANONYMOUS) {
                return authentication;
            }
        }
        return AnonymousAuthentication.getInstance();
    }
}
