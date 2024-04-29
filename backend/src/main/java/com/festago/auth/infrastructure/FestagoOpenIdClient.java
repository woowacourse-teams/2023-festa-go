package com.festago.auth.infrastructure;

import com.festago.auth.domain.OpenIdClient;
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
public class FestagoOpenIdClient implements OpenIdClient {

    private static final String PROFILE_IMAGE = "https://placehold.co/150x150";

    private final Map<String, Supplier<UserInfo>> userInfoMap = new HashMap<>();

    public FestagoOpenIdClient() {
        userInfoMap.put("1", () -> new UserInfo("1", getSocialType(), "member1", PROFILE_IMAGE));
        userInfoMap.put("2", () -> new UserInfo("2", getSocialType(), "member2", PROFILE_IMAGE));
        userInfoMap.put("3", () -> new UserInfo("3", getSocialType(), "member3", PROFILE_IMAGE));
    }

    @Override
    public UserInfo getUserInfo(String idToken) {
        return userInfoMap.getOrDefault(idToken, () -> {
            throw new BadRequestException(ErrorCode.OPEN_ID_INVALID_TOKEN);
        }).get();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.FESTAGO;
    }
}
