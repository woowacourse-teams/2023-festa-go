package com.festago.auth.domain;

public interface AuthProvider {

    String provide(AuthPayload member);
}
