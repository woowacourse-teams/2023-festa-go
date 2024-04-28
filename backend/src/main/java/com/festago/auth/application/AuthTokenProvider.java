package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.v1.TokenResponse;

public interface AuthTokenProvider {

    TokenResponse provide(AuthPayload authPayload);
}
