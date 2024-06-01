package com.festago.fcm.application.command;

import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.domain.MemberFCMExpiredAtPolicy;
import com.festago.fcm.domain.MemberFCMRemoveOldTokensPolicy;
import com.festago.fcm.repository.MemberFCMRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberFCMCommandService {

    private final MemberFCMRepository memberFCMRepository;
    private final MemberFCMExpiredAtPolicy memberFCMExpiredAtPolicy;
    private final MemberFCMRemoveOldTokensPolicy memberFCMDeleteOldTokensPolicy;

    // TODO 별도의 Service 클래스 생성해서 비즈니스 로직 이관 생각해 볼 것
    public void registerFCM(Long memberId, String fcmToken) {
        List<MemberFCM> memberFCMs = memberFCMRepository.findAllByMemberId(memberId);
        LocalDateTime expiredAt = memberFCMExpiredAtPolicy.provide();
        memberFCMs.stream()
            .filter(it -> it.isSameToken(fcmToken))
            .findAny()
            .ifPresentOrElse(memberFCM -> {
                memberFCM.changeExpiredAt(expiredAt);
            }, () -> {
                memberFCMDeleteOldTokensPolicy.delete(memberFCMs);
                memberFCMRepository.save(new MemberFCM(memberId, fcmToken, expiredAt));
            });
    }

    public void deleteAllMemberFCM(Long memberId) {
        memberFCMRepository.deleteAllByMemberId(memberId);
    }
}
