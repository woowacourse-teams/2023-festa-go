package com.festago.auth.application;

import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final LoginService loginService;
    private final OAuth2Clients oAuth2Clients;
    private final MemberRepository memberRepository;

    public AuthService(LoginService loginService, OAuth2Clients oAuth2Clients,
                       MemberRepository memberRepository) {
        this.loginService = loginService;
        this.oAuth2Clients = oAuth2Clients;
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest request) {
        return loginService.login(getUserInfo(request));
    }

    private UserInfo getUserInfo(LoginRequest request) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(request.socialType());
        return oAuth2Client.getUserInfo(request.accessToken());
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
