package com.festago.fcm.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.fcm.application.FcmClient;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
@Slf4j
public class FcmClientImpl implements FcmClient {

    private static final int BATCH_ALERT_SIZE = 500;

    private final FirebaseMessaging firebaseMessaging;
    private final Executor taskExecutor;

    @Override
    public void sendAll(List<String> tokens, FCMChannel channel, FcmPayload payload) {
        validateEmptyTokens(tokens);
        List<Message> messages = createMessages(tokens, channel, payload);

        if (messages.size() <= BATCH_ALERT_SIZE) {
            sendMessages(messages, channel);
            return;
        }

        for (int i = 0; i < messages.size(); i += BATCH_ALERT_SIZE) {
            List<Message> batchMessages = messages.subList(i, Math.min(i + BATCH_ALERT_SIZE, messages.size()));
            taskExecutor.execute(() -> sendMessages(batchMessages, channel));
        }
    }

    private void validateEmptyTokens(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new InternalServerException(ErrorCode.FCM_NOT_FOUND);
        }
    }

    public void sendMessages(List<Message> messages, FCMChannel channel) {
        try {
            BatchResponse response = firebaseMessaging.sendAll(messages);
            checkAllSuccess(response, channel);
        } catch (FirebaseMessagingException e) {
            log.warn("[FCM: {}] 전송 실패: {}", channel, e.getMessagingErrorCode());
        }
    }

    private List<Message> createMessages(List<String> tokens, FCMChannel channel, FcmPayload payload) {
        return tokens.stream()
            .map(token -> createMessage(token, channel, payload))
            .toList();
    }

    private Message createMessage(String token, FCMChannel channel, FcmPayload payload) {
        return Message.builder()
            .setAndroidConfig(createAndroidConfig(channel, payload))
            .setToken(token)
            .build();
    }

    private AndroidConfig createAndroidConfig(FCMChannel channel, FcmPayload payload) {
        return AndroidConfig.builder()
            .setNotification(createAndroidNotification(channel, payload))
            .build();
    }

    private AndroidNotification createAndroidNotification(FCMChannel channel, FcmPayload payload) {
        return AndroidNotification.builder()
            .setTitle(payload.title())
            .setBody(payload.body())
            .setChannelId(channel.toString())
            .build();
    }

    private void checkAllSuccess(BatchResponse batchResponse, FCMChannel channel) {
        int failCount = batchResponse.getFailureCount();
        if (failCount == 0) {
            return;
        }
        log.warn("[FCM: {}] {}건의 요청들이 실패했습니다.", channel, failCount);
    }
}
