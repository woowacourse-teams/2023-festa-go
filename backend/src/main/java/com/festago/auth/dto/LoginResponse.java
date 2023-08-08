package com.festago.auth.dto;

public record LoginResponse(
    String accessToken,
    String nickname,
    boolean isNew
) {

    public static LoginResponse isNew(String accessToken, String nickname) {
        return new LoginResponse(accessToken, nickname, true);
    }

    public static LoginResponse isExists(String accessToken, String nickname) {
        return new LoginResponse(accessToken, nickname, false);
    }
}
