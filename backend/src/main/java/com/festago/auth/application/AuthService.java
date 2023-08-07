package com.festago.auth.application;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMember;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Clients oAuth2Clients;
    private final AuthProvider authProvider;
    private final AuthExtractor authExtractor;

    public AuthService(MemberRepository memberRepository, OAuth2Clients oAuth2Clients, AuthProvider authProvider,
                       AuthExtractor authExtractor) {
        this.memberRepository = memberRepository;
        this.oAuth2Clients = oAuth2Clients;
        this.authProvider = authProvider;
        this.authExtractor = authExtractor;
    }

    public LoginResponse login(LoginRequest request) {
        UserInfo userInfo = getUserInfo(request);
        Member member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .orElseGet(() -> signUp(userInfo));
        return new LoginResponse(authProvider.provide(new AuthPayload(member.getId())), member.getNickname());
    }

    private UserInfo getUserInfo(LoginRequest request) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(request.socialType());
        String accessToken = oAuth2Client.getAccessToken(request.code());
        return oAuth2Client.getUserInfo(accessToken);
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

    public LoginMember loginMemberByHeader(String header) {
        Member member = findMemberById(authExtractor.extract(header).getMemberId());
        return new LoginMember(member.getId());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
