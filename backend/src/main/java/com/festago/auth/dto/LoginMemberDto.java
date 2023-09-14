package com.festago.auth.dto;

import com.festago.zmember.domain.Member;

public record LoginMemberDto(
    boolean isNew,
    Long memberId,
    String nickname
) {

    public static LoginMemberDto isNew(Member member) {
        return new LoginMemberDto(true, member.getId(), member.getNickname());
    }

    public static LoginMemberDto isExists(Member member) {
        return new LoginMemberDto(false, member.getId(), member.getNickname());
    }
}
