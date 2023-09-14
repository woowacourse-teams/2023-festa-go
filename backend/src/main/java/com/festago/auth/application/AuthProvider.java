package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;

public interface AuthProvider {

    String provide(AuthPayload authPayload);
}
