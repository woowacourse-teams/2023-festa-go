package com.festago.auth.application;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.event.MemberDeletedEvent;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public LoginMemberDto login(UserInfo userInfo) {
        Optional<Member> originMember =
            memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType());
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
        logDeleteMember(member);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(member));
    }

    private void logDeleteMember(Member member) {
        log.info("[DELETE MEMBER] memberId: {} / socialType: {} / socialId: {}",
            member.getId(), member.getSocialType(), member.getSocialId());
    }
}
