package com.festago.auth.dto.v1;

public record TokenRefreshV1Response(
    TokenResponse accessToken,
    TokenResponse refreshToken
) {

}
