package com.festago.auth.domain;

import com.festago.auth.dto.KakaoUserInfo;
import com.festago.domain.Member;

public record UserInfo(
    String socialId,
    SocialType socialType,
    String nickname,
    String profileImage
) {

    public Member toMember() {
        return new Member(
            socialId,
            socialType,
            nickname,
            profileImage
        );
    }

    public static UserInfo ofKakao(KakaoUserInfo kakaoUserInfo) {
        return new UserInfo(
            kakaoUserInfo.id(),
            SocialType.KAKAO,
            kakaoUserInfo.kakaoAccount().profile().nickname(),
            kakaoUserInfo.kakaoAccount().profile().thumbnailImageUrl());
    }
}
