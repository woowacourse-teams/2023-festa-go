package com.festago.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
    String id,
    @JsonProperty(value = "kakao_account") KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
        Profile profile
    ) {

        public record Profile(
            String nickname,
            @JsonProperty(value = "thumbnail_image_url") String thumbnailImageUrl
        ) {

        }
    }
}
