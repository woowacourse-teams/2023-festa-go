package com.festago.auth.application.command;

import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.OpenIdClients;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.domain.authentication.MemberAuthentication;
import com.festago.auth.dto.v1.LoginV1Response;
import com.festago.auth.dto.v1.MemberLoginResult;
import com.festago.auth.dto.v1.TokenRefreshResult;
import com.festago.auth.dto.v1.TokenRefreshV1Response;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.infrastructure.MemberAuthenticationTokenProvider;
import com.festago.member.domain.SocialType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthFacadeService {

    private final OAuth2Clients oAuth2Clients;
    private final OpenIdClients openIdClients;
    private final MemberAuthCommandService memberAuthCommandService;
    private final MemberAuthenticationTokenProvider authTokenProvider;

    public LoginV1Response oAuth2Login(SocialType socialType, String code) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(socialType);
        UserInfo userInfo = oAuth2Client.getUserInfo(code);
        return login(userInfo);
    }

    private LoginV1Response login(UserInfo userInfo) {
        MemberLoginResult loginResult = memberAuthCommandService.login(userInfo);

        TokenResponse accessToken = authTokenProvider.provide(new MemberAuthentication(loginResult.memberId()));
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

    public LoginV1Response openIdLogin(SocialType socialType, String idToken) {
        OpenIdClient openIdClient = openIdClients.getClient(socialType);
        UserInfo userInfo = openIdClient.getUserInfo(idToken);
        return login(userInfo);
    }

    public void logout(Long memberId, UUID refreshTokenId) {
        memberAuthCommandService.logout(memberId, refreshTokenId);
    }

    public TokenRefreshV1Response refresh(UUID refreshTokenId) {
        TokenRefreshResult tokenRefreshResult = memberAuthCommandService.refresh(refreshTokenId);
        Long memberId = tokenRefreshResult.memberId();
        TokenResponse accessToken = authTokenProvider.provide(new MemberAuthentication(memberId));
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
