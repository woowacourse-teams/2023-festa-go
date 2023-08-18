package com.festago.auth.domain;

public interface AuthExtractor {

    AuthPayload extract(String token);
}
