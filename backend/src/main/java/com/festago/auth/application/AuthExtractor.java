package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;

public interface AuthExtractor {

    AuthPayload extract(String token);
}
