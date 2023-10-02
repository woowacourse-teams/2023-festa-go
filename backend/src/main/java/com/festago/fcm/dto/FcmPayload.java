package com.festago.fcm.dto;

public record FcmPayload(
    String title,
    String body
) {

    public static FcmPayload entryAlert() {
        return new FcmPayload(
            "🎫 입장 안내",
            "잠시 후 입장인 무대가 있습니다!"
        );
    }

    public static FcmPayload entryProcess() {
        return new FcmPayload(
            "🎉 입장 완료",
            "즐거운 축제 관람 되세요!"
        );
    }
}
