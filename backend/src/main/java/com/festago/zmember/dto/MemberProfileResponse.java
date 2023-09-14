package com.festago.zmember.dto;

import com.festago.zmember.domain.Member;

public record MemberProfileResponse(
    Long memberId,
    String nickname,
    String profileImage
) {

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(member.getId(), member.getNickname(), member.getProfileImage());
    }
}
