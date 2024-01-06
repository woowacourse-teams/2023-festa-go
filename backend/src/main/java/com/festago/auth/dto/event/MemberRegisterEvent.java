package com.festago.auth.dto.event;

public record MemberRegisterEvent(
    String accessToken,
    String fcmToken
) {

}
