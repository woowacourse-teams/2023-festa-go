package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeService {

    private final AuthService authService;
    private final OAuth2Clients oAuth2Clients;
    private final AuthProvider authProvider;

    public LoginResponse login(LoginRequest request) {
        LoginMemberDto loginMember = authService.login(getUserInfo(request));
        String accessToken = getAccessToken(loginMember.memberId());
        return LoginResponse.of(accessToken, loginMember);
    }

    private String getAccessToken(Long memberId) {
        return authProvider.provide(new AuthPayload(memberId, Role.MEMBER));
    }

    private UserInfo getUserInfo(LoginRequest request) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(request.socialType());
        return oAuth2Client.getUserInfo(request.accessToken());
    }

    public void deleteMember(Long memberId) {
        authService.deleteMember(memberId);
    }
}
