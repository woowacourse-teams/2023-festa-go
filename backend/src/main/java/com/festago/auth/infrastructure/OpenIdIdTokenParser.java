package com.festago.auth.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OpenIdIdTokenParser {

    private final JwtParser jwtParser;

    public Claims parse(String idToken) {
        try {
            return jwtParser.parseSignedClaims(idToken).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.info("OpenID Token 파싱에서 예외가 발생했습니다. message={}", e.getMessage());
            throw new UnauthorizedException(ErrorCode.OPEN_ID_INVALID_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.", e);
            throw e;
        }
    }
}
