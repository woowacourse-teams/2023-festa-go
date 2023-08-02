package com.festago.auth.infrastructure;

import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2Client implements OAuth2Client {

    private final KakaoOAuth2AccessTokenClient accessTokenClient;
    private final KakaoOAuth2UserInfoClient userInfoClient;

    public KakaoOAuth2Client(KakaoOAuth2AccessTokenClient accessTokenClient, KakaoOAuth2UserInfoClient userInfoClient) {
        this.accessTokenClient = accessTokenClient;
        this.userInfoClient = userInfoClient;
    }

    @Override
    public String getAccessToken(String code) {
        return accessTokenClient.getAccessToken(code);
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        return userInfoClient.getUserInfo(accessToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
