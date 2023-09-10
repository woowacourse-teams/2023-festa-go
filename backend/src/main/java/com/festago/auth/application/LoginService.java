package com.festago.auth.application;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMemberDto login(UserInfo userInfo) {
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(),
            userInfo.socialType());
        if (optionalMember.isPresent()) {
            return LoginMemberDto.isExists(optionalMember.get());
        }
        Member member = signUp(userInfo);
        return LoginMemberDto.isNew(member);
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }
}
