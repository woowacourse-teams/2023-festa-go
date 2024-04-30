package com.festago.auth.infrastructure;

import com.festago.auth.domain.OpenIdNonceValidator;
import com.festago.auth.domain.OpenIdUserInfoProvider;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KakaoOpenIdUserInfoProvider implements OpenIdUserInfoProvider {

    private static final String ISSUER = "https://kauth.kakao.com";
    private final OpenIdNonceValidator openIdNonceValidator;
    private final JwtParser jwtParser;

    public KakaoOpenIdUserInfoProvider(
        @Value("${festago.oauth2.kakao.client-id}") String clientId,
        KakaoOpenIdPublicKeyLocator kakaoOpenIdPublicKeyLocator,
        OpenIdNonceValidator openIdNonceValidator,
        Clock clock
    ) {
        this.openIdNonceValidator = openIdNonceValidator;
        this.jwtParser = Jwts.parser()
            .keyLocator(kakaoOpenIdPublicKeyLocator)
            .requireIssuer(ISSUER)
            .requireAudience(clientId)
            .clock(() -> Date.from(clock.instant()))
            .build();
    }

    @Override
    public UserInfo provide(String idToken) {
        Claims payload = getPayload(idToken);
        openIdNonceValidator.validate(payload.get("nonce", String.class), payload.getExpiration());
        return UserInfo.builder()
            .socialType(SocialType.KAKAO)
            .socialId(payload.getSubject())
            .nickname(payload.get("nickname", String.class))
            .profileImage(payload.get("picture", String.class))
            .build();
    }

    private Claims getPayload(String idToken) {
        try {
            return jwtParser.parseSignedClaims(idToken)
                .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorCode.OPEN_ID_INVALID_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.", e);
            throw e;
        }
    }
}
