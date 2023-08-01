package com.festago.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;

public record KakaoUserInfo(
    String id,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {

    public UserInfo toUserInfo() {
        return new UserInfo(
            id,
            SocialType.KAKAO,
            kakaoAccount.profile.nickname,
            kakaoAccount.profile.thumbnailImageUrl
        );
    }

    public record KakaoAccount(
        Profile profile
    ) {

        public record Profile(
            String nickname,
            @JsonProperty("thumbnail_image_url") String thumbnailImageUrl
        ) {

        }
    }
}
