package com.festago.auth.application.command;

import com.festago.auth.application.AuthTokenProvider;
import com.festago.auth.application.OAuth2Client;
import com.festago.auth.application.OAuth2Clients;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.v1.LoginResult;
import com.festago.auth.dto.v1.LoginV1Response;
import com.festago.auth.dto.v1.TokenRefreshResult;
import com.festago.auth.dto.v1.TokenRefreshV1Response;
import com.festago.auth.dto.v1.TokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthFacadeService {

    private final OAuth2Clients oAuth2Clients;
    private final MemberAuthCommandService memberAuthCommandService;
    private final AuthTokenProvider authTokenProvider;

    public LoginV1Response oAuth2Login(SocialType socialType, String code) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(socialType);
        String oAuth2AccessToken = oAuth2Client.getAccessToken(code);
        UserInfo userInfo = oAuth2Client.getUserInfo(oAuth2AccessToken);

        LoginResult loginResult = memberAuthCommandService.oAuth2Login(userInfo);

        TokenResponse accessToken = authTokenProvider.provide(new AuthPayload(loginResult.memberId(), Role.MEMBER));
        return new LoginV1Response(
            loginResult.nickname(),
            loginResult.profileImageUrl(),
            accessToken,
            new TokenResponse(
                loginResult.refreshToken().toString(),
                loginResult.refreshTokenExpiredAt()
            )
        );
    }

    public void logout(Long memberId, UUID refreshTokenId) {
        memberAuthCommandService.logout(memberId, refreshTokenId);
    }

    public TokenRefreshV1Response refresh(UUID refreshTokenId) {
        TokenRefreshResult tokenRefreshResult = memberAuthCommandService.refresh(refreshTokenId);
        Long memberId = tokenRefreshResult.memberId();
        TokenResponse accessToken = authTokenProvider.provide(new AuthPayload(memberId, Role.MEMBER));
        return new TokenRefreshV1Response(
            accessToken,
            new TokenResponse(
                tokenRefreshResult.token(),
                tokenRefreshResult.expiredAt()
            )
        );
    }

    public void deleteAccount(Long memberId) {
        memberAuthCommandService.deleteAccount(memberId);
    }
}
