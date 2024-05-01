package com.festago.auth.infrastructure;

import com.festago.auth.domain.OpenIdNonceValidator;
import com.festago.auth.domain.OpenIdUserInfoProvider;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import io.jsonwebtoken.Claims;
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
    private final OpenIdIdTokenParser idTokenParser;

    public KakaoOpenIdUserInfoProvider(
        @Value("${festago.oauth2.kakao.client-id}") String clientId,
        KakaoOpenIdPublicKeyLocator kakaoOpenIdPublicKeyLocator,
        OpenIdNonceValidator openIdNonceValidator,
        Clock clock
    ) {
        this.openIdNonceValidator = openIdNonceValidator;
        this.idTokenParser = new OpenIdIdTokenParser(Jwts.parser()
            .keyLocator(kakaoOpenIdPublicKeyLocator)
            .requireIssuer(ISSUER)
            .requireAudience(clientId)
            .clock(() -> Date.from(clock.instant()))
            .build());
    }

    @Override
    public UserInfo provide(String idToken) {
        Claims payload = idTokenParser.parse(idToken);
        openIdNonceValidator.validate(payload.get("nonce", String.class), payload.getExpiration());
        return UserInfo.builder()
            .socialType(SocialType.KAKAO)
            .socialId(payload.getSubject())
            .nickname(payload.get("nickname", String.class))
            .profileImage(payload.get("picture", String.class))
            .build();
    }
}
