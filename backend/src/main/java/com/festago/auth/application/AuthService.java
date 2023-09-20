package com.festago.auth.application;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.event.MemberDeleteEvent;
import com.festago.auth.dto.event.MemberLoginEvent;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    public AuthService(MemberRepository memberRepository, ApplicationEventPublisher publisher) {
        this.memberRepository = memberRepository;
        this.publisher = publisher;
    }

    public LoginMemberDto login(UserInfo userInfo, String fcmToken) {
        LoginMemberDto loginMemberDto = handleLoginRequest(userInfo);
        publisher.publishEvent(new MemberLoginEvent(loginMemberDto.memberId(), fcmToken));
        return loginMemberDto;
    }

    public LoginMemberDto handleLoginRequest(UserInfo userInfo) {
        Optional<Member> originMember = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType());
        if (originMember.isPresent()) {
            Member member = originMember.get();
            return LoginMemberDto.isExists(member);
        }
        Member newMember = signUp(userInfo);
        return LoginMemberDto.isNew(newMember);
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
        publisher.publishEvent(new MemberDeleteEvent(member.getId()));
    }
}
