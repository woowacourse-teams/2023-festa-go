package com.festago.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;

public record KakaoUserInfo(
    String id,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {

    public UserInfo toUserInfo() {
        return UserInfo.builder()
            .socialId(id)
            .socialType(SocialType.KAKAO)
            .nickname(kakaoAccount.profile.nickname)
            .profileImage(kakaoAccount.profile.thumbnailImageUrl)
            .build();
    }

    public record KakaoAccount(
        Profile profile
    ) {

        public record Profile(
            String nickname,
            @JsonProperty("thumbnail_image_url")
            String thumbnailImageUrl
        ) {

        }
    }
}
