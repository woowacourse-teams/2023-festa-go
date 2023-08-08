package com.festago.auth.domain;

public interface OAuth2Client {

    String getAccessToken(String code);

    UserInfo getUserInfo(String accessToken);

    SocialType getSocialType();
}
