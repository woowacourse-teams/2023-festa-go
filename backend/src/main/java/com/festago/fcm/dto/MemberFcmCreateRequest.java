package com.festago.fcm.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberFcmCreateRequest(
    @NotBlank(message = "fcmToken은 공백일 수 없습니다.")
    String fcmToken
) {

}
