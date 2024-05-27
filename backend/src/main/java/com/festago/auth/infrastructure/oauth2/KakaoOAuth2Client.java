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

    private final KakaoOAuth2TokenClient kakaoOAuth2TokenClient;
    private final KakaoOpenIdClient kakaoOpenIdClient;

    @Override
    public UserInfo getUserInfo(String code) {
        String idToken = kakaoOAuth2TokenClient.getIdToken(code);
        return kakaoOpenIdClient.getUserInfo(idToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
