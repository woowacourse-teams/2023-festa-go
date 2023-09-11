package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
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

    public LoginResponse login(LoginRequest request) {
        LoginMemberDto loginMember = authService.login(getUserInfo(request));
        Member member = loginMember.member();
        String accessToken = getAccessToken(member);
        if (loginMember.isNew()) {
            return LoginResponse.isNew(accessToken, member.getNickname());
        }
        return LoginResponse.isExists(accessToken, member.getNickname());
    }

    private String getAccessToken(Member member) {
        return authProvider.provide(new AuthPayload(member.getId(), Role.MEMBER));
    }

    private UserInfo getUserInfo(LoginRequest request) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(request.socialType());
        return oAuth2Client.getUserInfo(request.accessToken());
    }

    public void deleteMember(Long memberId) {
        authService.deleteMember(memberId);
    }
}
