package com.festago.auth.infrastructure.oauth2;

import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.UserInfo;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.member.domain.SocialType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class FestagoOAuth2Client implements OAuth2Client {

    private final Map<String, UserInfo> userInfoMap = new HashMap<>();

    public FestagoOAuth2Client() {
        userInfoMap.put("1", new UserInfo("1", getSocialType(), "member1", null));
        userInfoMap.put("2", new UserInfo("2", getSocialType(), "member2", null));
        userInfoMap.put("3", new UserInfo("3", getSocialType(), "member3", null));
    }

    @Override
    public UserInfo getUserInfo(String code) {
        UserInfo userInfo = userInfoMap.get(code);
        if (userInfo == null) {
            throw new BadRequestException(ErrorCode.OAUTH2_INVALID_TOKEN);
        }
        return userInfo;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.FESTAGO;
    }
}
