package com.festago.entryalert.application;

import com.festago.fcm.application.FcmClient;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EntryAlertFcmClient {

    private final FcmClient fcmClient;
    private final EntryAlertStatusService entryAlertStatusService;

    public EntryAlertFcmClient(FcmClient fcmClient, EntryAlertStatusService entryAlertStatusService) {
        this.fcmClient = fcmClient;
        this.entryAlertStatusService = entryAlertStatusService;
    }

    @Async
    public void sendAll(Long entryAlertId, List<String> tokens, FCMChannel channel, FcmPayload fcmPayload) {
        boolean isSuccess = fcmClient.sendAll(tokens, channel, fcmPayload);
        entryAlertStatusService.updateRequestedEntryAlertStatus(entryAlertId, isSuccess);
    }
}
