package com.festago.auth.domain;

public interface OAuth2Client {

    UserInfo getUserInfo(String accessToken);

    SocialType getSocialType();
}
