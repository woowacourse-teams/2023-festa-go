package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeService {

    private final AuthService authService;
    private final OAuth2Clients oAuth2Clients;
    private final AuthProvider authProvider;

    public AuthFacadeService(AuthService authService, OAuth2Clients oAuth2Clients,
                             AuthProvider authProvider) {
        this.authService = authService;
        this.oAuth2Clients = oAuth2Clients;
        this.authProvider = authProvider;
    }

    public LoginResponse login(SocialType socialType, String oAuthToken) {
        UserInfo userInfo = getUserInfo(socialType, oAuthToken);
        LoginMemberDto loginMember = authService.login(userInfo);
        String accessToken = getAccessToken(loginMember.memberId());
        return LoginResponse.of(accessToken, loginMember);
    }

    private String getAccessToken(Long memberId) {
        return authProvider.provide(new AuthPayload(memberId, Role.MEMBER));
    }

    private UserInfo getUserInfo(SocialType socialType, String oAuthToken) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(socialType);
        return oAuth2Client.getUserInfo(oAuthToken);
    }

    public void deleteMember(Long memberId) {
        authService.deleteMember(memberId);
    }
}
