package com.festago.dto;

import com.festago.domain.Member;

public record MemberResponse(
    String nickname,
    String profileImage
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getNickname(), member.getProfileImage());
    }
}
