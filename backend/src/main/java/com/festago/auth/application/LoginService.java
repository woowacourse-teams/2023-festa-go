package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginResponse;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;
    private final AuthProvider authProvider;

    public LoginService(MemberRepository memberRepository, AuthProvider authProvider) {
        this.memberRepository = memberRepository;
        this.authProvider = authProvider;
    }

    public LoginResponse login(UserInfo userInfo) {
        return memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .map(member -> LoginResponse.isExists(getAccessToken(member), member.getNickname()))
            .orElseGet(() -> {
                Member member = signUp(userInfo);
                return LoginResponse.isNew(getAccessToken(member), member.getNickname());
            });
    }

    private String getAccessToken(Member member) {
        return authProvider.provide(new AuthPayload(member.getId(), Role.MEMBER));
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

}
