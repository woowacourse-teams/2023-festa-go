package com.festago.zmember.application;

import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import com.festago.zmember.domain.Member;
import com.festago.zmember.dto.MemberProfileResponse;
import com.festago.zmember.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberProfileResponse findMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberProfileResponse.from(member);
    }
}
