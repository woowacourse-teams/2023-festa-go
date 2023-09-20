package com.festago.fcm.dto;

import com.festago.fcm.domain.MemberFCM;

public record MemberFCMResponse(
    Long id,
    Long memberId,
    String fcmToken
) {

    public static MemberFCMResponse from(MemberFCM memberFCM) {
        return new MemberFCMResponse(
            memberFCM.getId(),
            memberFCM.getMemberId(),
            memberFCM.getFcmToken()
        );
    }
}
