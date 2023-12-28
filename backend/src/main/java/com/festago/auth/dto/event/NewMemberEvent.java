package com.festago.auth.dto.event;

public record NewMemberEvent(
    boolean isNew,
    String accessToken,
    String fcmToken
) {

}
