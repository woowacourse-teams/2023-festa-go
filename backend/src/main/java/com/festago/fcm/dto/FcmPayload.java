package com.festago.fcm.dto;

public record FcmPayload(
    String title,
    String body
) {

    public static FcmPayload empty() {
        return new FcmPayload(
            null,
            null
        );
    }

    public static FcmPayload entryAlert() {
        return new FcmPayload(
            "🎫 입장 안내",
            "잠시 후 입장인 무대가 있습니다!"
        );
    }
}
