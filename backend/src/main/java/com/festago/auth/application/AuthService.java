package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Clients oAuth2Clients;
    private final AuthProvider authProvider;

    public AuthService(MemberRepository memberRepository, OAuth2Clients oAuth2Clients,
                       AuthProvider authProvider) {
        this.memberRepository = memberRepository;
        this.oAuth2Clients = oAuth2Clients;
        this.authProvider = authProvider;
    }

    public LoginResponse login(LoginRequest request) {
        UserInfo userInfo = getUserInfo(request);
        return memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .map(member -> LoginResponse.isExists(getAccessToken(member), member.getNickname()))
            .orElseGet(() -> {
                Member member = signUp(userInfo);
                return LoginResponse.isNew(getAccessToken(member), member.getNickname());
            });
    }

    private UserInfo getUserInfo(LoginRequest request) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(request.socialType());
        return oAuth2Client.getUserInfo(request.accessToken());
    }

    private String getAccessToken(Member member) {
        return authProvider.provide(new AuthPayload(member.getId()));
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }
}
