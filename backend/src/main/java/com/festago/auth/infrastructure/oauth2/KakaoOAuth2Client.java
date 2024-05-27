package com.festago.auth.infrastructure.oauth2;

import com.festago.auth.application.OAuth2Client;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.infrastructure.openid.KakaoOpenIdClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client {

    private final KakaoOAuth2AccessTokenClient accessTokenClient;
    private final KakaoOpenIdClient openIdClient;

    @Override
    public String getAccessToken(String code) {
        return accessTokenClient.getAccessToken(code);
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        return openIdClient.getUserInfo(accessToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
