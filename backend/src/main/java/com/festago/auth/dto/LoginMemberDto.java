package com.festago.auth.dto;

import com.festago.domain.Member;

public record LoginMemberDto(
    boolean isNew,
    Member member
) {

    public static LoginMemberDto isNew(Member member) {
        return new LoginMemberDto(true, member);
    }

    public static LoginMemberDto isExists(Member member) {
        return new LoginMemberDto(false, member);
    }
}
