package com.festago.auth.dto.event;

import com.festago.member.domain.Member;

public record MemberCreatedEvent(
    Member member
) {

}
