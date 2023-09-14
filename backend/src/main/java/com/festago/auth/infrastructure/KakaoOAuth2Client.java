package com.festago.auth.infrastructure;

import com.festago.auth.application.OAuth2Client;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2Client implements OAuth2Client {

    private final KakaoOAuth2UserInfoClient userInfoClient;

    public KakaoOAuth2Client(KakaoOAuth2UserInfoClient userInfoClient) {
        this.userInfoClient = userInfoClient;
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
