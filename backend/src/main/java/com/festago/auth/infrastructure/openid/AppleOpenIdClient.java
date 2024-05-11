package com.festago.auth.infrastructure.openid;

import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOpenIdClient implements OpenIdClient {

    private final AppleOpenIdUserInfoProvider appleOpenIdUserInfoProvider;

    @Override
    public UserInfo getUserInfo(String idToken) {
        return appleOpenIdUserInfoProvider.provide(idToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.APPLE;
    }
}
