package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;

public interface AuthTokenExtractor {

    AuthPayload extract(String token);
}
