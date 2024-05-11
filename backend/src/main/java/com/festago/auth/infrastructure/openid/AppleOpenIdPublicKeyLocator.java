package com.festago.auth.infrastructure.openid;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOpenIdPublicKeyLocator implements Locator<Key> {

    private final AppleOpenIdJwksClient appleOpenIdJwksClient;
    private final CachedAppleOpenIdKeyProvider cachedOpenIdKeyProvider;

    @Override
    public Key locate(Header header) {
        String kid = (String) header.get("kid");
        if (kid == null) {
            throw new UnauthorizedException(ErrorCode.OPEN_ID_INVALID_TOKEN);
        }
        return cachedOpenIdKeyProvider.provide(kid, appleOpenIdJwksClient::requestGetJwks);
    }
}
