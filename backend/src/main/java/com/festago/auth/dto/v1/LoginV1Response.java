package com.festago.auth.dto.v1;

public record LoginV1Response(
    String nickname,
    String profileImageUrl,
    TokenResponse accessToken,
    TokenResponse refreshToken
) {

}
