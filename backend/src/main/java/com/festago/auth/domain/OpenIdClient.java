package com.festago.auth.domain;

public interface OpenIdClient {

    UserInfo getUserInfo(String idToken);

    SocialType getSocialType();
}
