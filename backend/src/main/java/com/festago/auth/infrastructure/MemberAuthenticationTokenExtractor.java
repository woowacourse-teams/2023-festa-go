package com.festago.auth.infrastructure;

import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.jwt.JwtTokenParser;
import com.festago.auth.infrastructure.jwt.MemberAuthenticationClaimsExtractor;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final MemberAuthenticationClaimsExtractor memberAuthenticationClaimsExtractor;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        return memberAuthenticationClaimsExtractor.extract(claims);
    }
}
