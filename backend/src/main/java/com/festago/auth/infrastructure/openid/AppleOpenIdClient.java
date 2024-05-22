package com.festago.auth.infrastructure.openid;

import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.OpenIdNonceValidator;
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
public class AppleOpenIdClient implements OpenIdClient {

    private static final String ISSUER = "https://appleid.apple.com";
    private final OpenIdNonceValidator openIdNonceValidator;
    private final OpenIdIdTokenParser idTokenParser;
    private final String clientId;

    public AppleOpenIdClient(
        @Value("${festago.oauth2.apple.client-id}") String appleClientId,
        AppleOpenIdPublicKeyLocator appleOpenIdPublicKeyLocator,
        OpenIdNonceValidator openIdNonceValidator,
        Clock clock
    ) {
        this.clientId = appleClientId;
        this.openIdNonceValidator = openIdNonceValidator;
        this.idTokenParser = new OpenIdIdTokenParser(Jwts.parser()
            .keyLocator(appleOpenIdPublicKeyLocator)
            .requireAudience(clientId)
            .requireIssuer(ISSUER)
            .clock(() -> Date.from(clock.instant()))
            .build());
    }

    @Override
    public UserInfo getUserInfo(String idToken) {
        Claims payload = idTokenParser.parse(idToken);
        openIdNonceValidator.validate(payload.get("nonce", String.class), payload.getExpiration());
        return UserInfo.builder()
            .socialType(SocialType.APPLE)
            .socialId(payload.getSubject())
            .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.APPLE;
    }
}
