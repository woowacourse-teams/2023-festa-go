package com.festago.auth.application;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberFCMRepository memberFCMRepository;

    public AuthService(MemberRepository memberRepository, MemberFCMRepository memberFCMRepository) {
        this.memberRepository = memberRepository;
        this.memberFCMRepository = memberFCMRepository;
    }

    public LoginMemberDto login(UserInfo userInfo, String fcmToken) {
        Optional<Member> loginMember = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType());
        if (loginMember.isPresent()) {
            Member member = loginMember.get();
            enrollFcm(member, fcmToken);
            return LoginMemberDto.isExists(member);
        }
        Member newMember = signUp(userInfo);
        enrollFcm(newMember, fcmToken);
        return LoginMemberDto.isNew(newMember);
    }

    private void enrollFcm(Member member, String fcmToken) {
        // TODO : 멤버의 모든 FCM 을 지우지 않기 위해서는 멤버의 디바이스에 대한 식별자가 필요합니다.
        deleteFCM(member);
        memberFCMRepository.save(new MemberFCM(member, fcmToken));
    }

    private void deleteFCM(Member member) {
        List<MemberFCM> originFCMs = memberFCMRepository.findByMemberId(member.getId());
        memberFCMRepository.deleteAll(originFCMs);
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
        deleteFCM(member);
    }
}
