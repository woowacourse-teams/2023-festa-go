package com.festago.auth.application;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMemberDto login(UserInfo userInfo) {
        return memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .map(LoginMemberDto::isExists)
            .orElseGet(() -> {
                Member member = signUp(userInfo);
                return LoginMemberDto.isNew(member);
            });
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
