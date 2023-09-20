package com.festago.auth.dto.event;

public record MemberLoginEvent(
    Long memberId,
    String fcmToken
) {

}
