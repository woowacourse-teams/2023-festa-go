package com.festago.fcm.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.member.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberFCMService {

    private final MemberFCMRepository memberFCMRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberFCMsResponse findMemberFCM(Long memberId) {
        List<MemberFCM> memberFCM = memberFCMRepository.findByMemberId(memberId);
        if (memberFCM.isEmpty()) {
            log.warn("member {} 의 FCM 토큰이 발급되지 않았습니다.", memberId);
        }
        return MemberFCMsResponse.from(memberFCM);
    }

    public void saveMemberFCM(Long memberId, String fcmToken) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (isNotExistsFcmToken(memberId, fcmToken)) {
            memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
        }
    }

    private boolean isNotExistsFcmToken(Long memberId, String fcmToken) {
        return !memberFCMRepository.existsByMemberIdAndFcmToken(memberId, fcmToken);
    }

    @Async
    public void deleteMemberFCM(Long memberId) {
        memberFCMRepository.deleteAllByMemberId(memberId);
    }
}
