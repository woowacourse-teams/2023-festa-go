package com.festago.member.dto;

import com.festago.member.domain.Member;

public record MemberProfileResponse(
    Long memberId,
    String nickname,
    String profileImage
) {

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(member.getId(), member.getNickname(), member.getProfileImage());
    }
}
