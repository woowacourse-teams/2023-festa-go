package com.festago.auth.dto;

public record LoginResponse(
    String accessToken,
    String nickname,
    boolean isNew
) {

    public static LoginResponse of(String accessToken, LoginMemberDto loginMember) {
        return new LoginResponse(accessToken, loginMember.member().getNickname(), loginMember.isNew());
    }
}
