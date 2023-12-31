package com.festago.auth.dto.event;

public record MemberRegisterEvent(
    boolean isNew,
    String accessToken,
    String fcmToken
) {

}
