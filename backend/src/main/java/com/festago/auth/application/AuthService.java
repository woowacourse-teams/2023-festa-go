package com.festago.auth.application;

import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Client oAuth2Client;
    private final AuthProvider authProvider;

    public AuthService(MemberRepository memberRepository, OAuth2Client oAuth2Client, AuthProvider authProvider) {
        this.memberRepository = memberRepository;
        this.oAuth2Client = oAuth2Client;
        this.authProvider = authProvider;
    }

    public LoginResponse login(String code) {
        String accessToken = oAuth2Client.getAccessToken(code);
        UserInfo userInfo = oAuth2Client.getUserInfo(accessToken);
        Member member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .orElseGet(() -> memberRepository.save(userInfo.toMember()));
        return new LoginResponse(authProvider.provide(member), userInfo.nickname());
    }
}
