package com.festago.fcm.application;

import com.festago.config.AsyncConfig;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Profile({"!prod", "!dev"})
public class MockFcmClient implements FcmClient {

    private static final Logger log = LoggerFactory.getLogger(MockFcmClient.class);

    @Override
    @Async(value = AsyncConfig.FCM_EXECUTOR_NAME)
    public void sendAll(List<String> tokens, FCMChannel channel, FcmPayload fcmPayload) {
        log.info("[FCM] title: {} / body: {} / to {} device", fcmPayload.title(), fcmPayload.body(), tokens.size());
    }
}
