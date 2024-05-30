package com.festago.auth.application;

import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;

public interface OAuth2Client {

    UserInfo getUserInfo(String code);

    SocialType getSocialType();
}
