package com.festago.fcm.application;

import com.festago.auth.dto.event.DeleteMemberEvent;
import com.festago.fcm.repository.MemberFCMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberDeletionEventListener {

    private final MemberFCMRepository memberFCMRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional
    public void deleteMember(DeleteMemberEvent event) {
        memberFCMRepository.deleteAllByMemberId(event.memberId());
    }
}
