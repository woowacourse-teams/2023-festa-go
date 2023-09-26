package com.festago.fcm.application;

import com.festago.auth.dto.event.MemberDeleteEvent;
import com.festago.auth.dto.event.MemberLoginEvent;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Profile({"dev", "prod"})
public class AuthEventListener {

    private final MemberFCMRepository memberFCMRepository;

    public AuthEventListener(MemberFCMRepository memberFCMRepository) {
        this.memberFCMRepository = memberFCMRepository;
    }

    @TransactionalEventListener
    public void saveMemberFCM(MemberLoginEvent memberLoginEvent) {
        Long memberId = memberLoginEvent.memberId();
        deleteFCM(memberId);
        memberFCMRepository.save(new MemberFCM(memberId, memberLoginEvent.fcmToken()));
    }

    private void deleteFCM(Long memberId) {
        List<MemberFCM> originFCMs = memberFCMRepository.findByMemberId(memberId);
        memberFCMRepository.deleteAll(originFCMs);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void deleteMemberFCM(MemberDeleteEvent memberLoginEvent) {
        deleteFCM(memberLoginEvent.memberId());
    }
}
