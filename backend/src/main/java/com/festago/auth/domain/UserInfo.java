package com.festago.auth.domain;

import com.festago.member.domain.Member;
import lombok.Builder;

@Builder
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
