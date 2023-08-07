package com.festago.auth.dto;

public record LoginResponse(
    String accessToken,
    String nickname
) {

}
