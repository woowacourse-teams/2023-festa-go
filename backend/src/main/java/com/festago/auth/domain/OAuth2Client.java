package com.festago.auth.domain;

import com.festago.member.domain.SocialType;

public interface OAuth2Client {

    UserInfo getUserInfo(String code);

    SocialType getSocialType();
}
