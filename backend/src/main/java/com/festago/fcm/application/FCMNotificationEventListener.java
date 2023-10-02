package com.festago.fcm.application;

import com.festago.config.AsyncConfig;
import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import com.festago.fcm.dto.MemberFCMResponse;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FCMNotificationEventListener {

    private final FcmClient fcmClient;
    private final MemberFCMService memberFCMService;

    public FCMNotificationEventListener(FcmClient fcmClient, MemberFCMService memberFCMService) {
        this.fcmClient = fcmClient;
        this.memberFCMService = memberFCMService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async(value = AsyncConfig.FCM_EXECUTOR_NAME)
    public void sendFcmNotification(EntryProcessEvent event) {
        List<String> tokens = getMemberFCMToken(event.memberId());
        fcmClient.sendAll(tokens, FCMChannel.ENTRY_PROCESS, FcmPayload.entryProcess());
    }

    private List<String> getMemberFCMToken(Long memberId) {
        return memberFCMService.findMemberFCM(memberId)
            .memberFCMs()
            .stream()
            .map(MemberFCMResponse::fcmToken)
            .toList();
    }
}
