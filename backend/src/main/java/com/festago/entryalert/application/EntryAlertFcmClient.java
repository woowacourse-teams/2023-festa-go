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
    private final EntryAlertResultHandler entryAlertResultHandler;

    public EntryAlertFcmClient(FcmClient fcmClient, EntryAlertResultHandler entryAlertResultHandler) {
        this.fcmClient = fcmClient;
        this.entryAlertResultHandler = entryAlertResultHandler;
    }

    @Async
    public void sendAll(Long entryAlertId, List<String> tokens, FCMChannel channel, FcmPayload fcmPayload) {
        boolean isSuccess = fcmClient.sendAll(tokens, channel, fcmPayload);
        entryAlertResultHandler.handle(entryAlertId, isSuccess);
    }
}
