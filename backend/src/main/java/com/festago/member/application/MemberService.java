package com.festago.member.application;

import com.festago.member.domain.Member;
import com.festago.member.dto.MemberProfileResponse;
import com.festago.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberProfileResponse findMemberProfile(Long memberId) {
        Member member = memberRepository.getOrThrow(memberId);
        return MemberProfileResponse.from(member);
    }
}
