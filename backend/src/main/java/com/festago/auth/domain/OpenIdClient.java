package com.festago.auth.domain;

import com.festago.member.domain.SocialType;

public interface OpenIdClient {

    UserInfo getUserInfo(String idToken);

    SocialType getSocialType();
}
