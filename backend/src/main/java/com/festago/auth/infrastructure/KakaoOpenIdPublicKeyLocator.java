package com.festago.auth.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOpenIdPublicKeyLocator implements Locator<Key> {

    private final KakaoOpenIdJwksClient kakaoOpenIdJwksClient;
    private final CachedOpenIdKeyProvider cachedOpenIdKeyProvider;

    @Override
    public Key locate(Header header) {
        String kid = (String) header.get("kid");
        if (kid == null) {
            throw new UnauthorizedException(ErrorCode.OPEN_ID_INVALID_TOKEN);
        }
        return cachedOpenIdKeyProvider.provide(kid, kakaoOpenIdJwksClient::requestGetJwks);
    }
}
