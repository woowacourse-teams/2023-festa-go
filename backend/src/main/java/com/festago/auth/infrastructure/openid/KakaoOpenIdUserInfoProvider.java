package com.festago.auth.infrastructure.openid;

import com.festago.auth.domain.OpenIdNonceValidator;
import com.festago.auth.domain.OpenIdUserInfoProvider;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.util.Date;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KakaoOpenIdUserInfoProvider implements OpenIdUserInfoProvider {

    private static final String ISSUER = "https://kauth.kakao.com";
    private final OpenIdNonceValidator openIdNonceValidator;
    private final OpenIdIdTokenParser idTokenParser;
    private final Set<String> appKeys;

    public KakaoOpenIdUserInfoProvider(
        @Value("${festago.oauth2.kakao.rest-api-key}") String restApiKey,
        @Value("${festago.oauth2.kakao.native-app-key}") String nativeAppKey,
        KakaoOpenIdPublicKeyLocator kakaoOpenIdPublicKeyLocator,
        OpenIdNonceValidator openIdNonceValidator,
        Clock clock
    ) {
        this.appKeys = Set.of(restApiKey, nativeAppKey);
        this.openIdNonceValidator = openIdNonceValidator;
        this.idTokenParser = new OpenIdIdTokenParser(Jwts.parser()
            .keyLocator(kakaoOpenIdPublicKeyLocator)
            .requireIssuer(ISSUER)
            .clock(() -> Date.from(clock.instant()))
            .build());
    }

    @Override
    public UserInfo provide(String idToken) {
        Claims payload = idTokenParser.parse(idToken);
        openIdNonceValidator.validate(payload.get("nonce", String.class), payload.getExpiration());
        validateAudience(payload.getAudience());
        return UserInfo.builder()
            .socialType(SocialType.KAKAO)
            .socialId(payload.getSubject())
            .nickname(payload.get("nickname", String.class))
            .profileImage(payload.get("picture", String.class))
            .build();
    }

    private void validateAudience(Set<String> audiences) {
        for (String audience : audiences) {
            if (appKeys.contains(audience)) {
                return;
            }
        }
        log.info("허용되지 않는 id 토큰의 audience 값이 요청되었습니다. audiences={}", audiences);
        throw new UnauthorizedException(ErrorCode.OPEN_ID_INVALID_TOKEN);
    }
}
