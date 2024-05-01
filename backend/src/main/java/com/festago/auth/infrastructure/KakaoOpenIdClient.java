package com.festago.auth.infrastructure;

import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOpenIdClient implements OpenIdClient {

    private final KakaoOpenIdUserInfoProvider kakaoIdTokenUserInfoProvider;

    @Override
    public UserInfo getUserInfo(String idToken) {
        return kakaoIdTokenUserInfoProvider.provide(idToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
