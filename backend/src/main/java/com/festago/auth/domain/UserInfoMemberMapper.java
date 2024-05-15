package com.festago.auth.domain;

import com.festago.member.domain.DefaultNicknamePolicy;
import com.festago.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class UserInfoMemberMapper {

    private final DefaultNicknamePolicy defaultNicknamePolicy;

    public Member toMember(UserInfo userInfo) {
        String nickname = userInfo.nickname();
        return new Member(
            userInfo.socialId(),
            userInfo.socialType(),
            StringUtils.hasText(nickname) ? nickname : defaultNicknamePolicy.generate(),
            userInfo.profileImage()
        );
    }
}
