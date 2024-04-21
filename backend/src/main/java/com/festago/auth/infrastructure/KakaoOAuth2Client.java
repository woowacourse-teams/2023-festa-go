package com.festago.auth.infrastructure;

import com.festago.auth.application.OAuth2Client;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client {

    private final KakaoOAuth2AccessTokenClient accessTokenClient;
    private final KakaoOAuth2UserInfoClient userInfoClient;

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
