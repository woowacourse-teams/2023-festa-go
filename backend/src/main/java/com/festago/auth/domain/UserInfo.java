package com.festago.auth.domain;

import com.festago.member.domain.SocialType;
import lombok.Builder;

@Builder
public record UserInfo(
    String socialId,
    SocialType socialType,
    String nickname,
    String profileImage
) {

}
