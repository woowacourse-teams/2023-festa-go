package com.festago.auth.domain;

public interface OpenIdUserInfoProvider {

    UserInfo provide(String idToken);
}
