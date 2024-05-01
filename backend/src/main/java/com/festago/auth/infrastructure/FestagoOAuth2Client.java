package com.festago.auth.infrastructure;

import com.festago.auth.application.OAuth2Client;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class FestagoOAuth2Client implements OAuth2Client {

    private static final String PROFILE_IMAGE = "https://placehold.co/150x150";

    private final Map<String, Supplier<UserInfo>> userInfoMap = new HashMap<>();

    public FestagoOAuth2Client() {
        userInfoMap.put("1", () -> new UserInfo("1", getSocialType(), "member1", PROFILE_IMAGE));
        userInfoMap.put("2", () -> new UserInfo("2", getSocialType(), "member2", PROFILE_IMAGE));
        userInfoMap.put("3", () -> new UserInfo("3", getSocialType(), "member3", PROFILE_IMAGE));
    }

    @Override
    public String getAccessToken(String code) {
        return code;
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        return userInfoMap.getOrDefault(accessToken, () -> {
            throw new BadRequestException(ErrorCode.OAUTH2_INVALID_TOKEN);
        }).get();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.FESTAGO;
    }
}
