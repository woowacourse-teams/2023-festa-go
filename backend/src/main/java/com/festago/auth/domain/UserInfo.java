package com.festago.auth.domain;

import com.festago.domain.Member;

public record UserInfo(
    String socialId,
    SocialType socialType,
    String nickname,
    String profileImage
) {

    public Member toMember() {
        return new Member(
            socialId,
            socialType,
            nickname,
            profileImage
        );
    }
}
