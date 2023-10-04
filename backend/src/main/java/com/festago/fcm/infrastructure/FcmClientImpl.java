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
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "prod"})
public class FcmClientImpl implements FcmClient {

    private static final Logger log = LoggerFactory.getLogger(FcmClientImpl.class);
    private static final int BATCH_ALERT_SIZE = 500;

    private final FirebaseMessaging firebaseMessaging;
    private final Executor taskExecutor;

    public FcmClientImpl(FirebaseMessaging firebaseMessaging, Executor taskExecutor) {
        this.firebaseMessaging = firebaseMessaging;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public boolean sendAll(List<String> tokens, FCMChannel channel, FcmPayload payload) {
        validateTokenSize(tokens);
        List<Message> messages = createMessages(tokens, channel, payload);

        int messageSize = messages.size();
        if (messageSize <= BATCH_ALERT_SIZE) {
            try {
                sendMessages(messages, channel);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < messages.size(); i += BATCH_ALERT_SIZE) {
            List<Message> batchMessages = messages.subList(i, Math.min(i + BATCH_ALERT_SIZE, messages.size()));
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                try {
                    sendMessages(batchMessages, channel);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }, taskExecutor);
            futures.add(future);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.join();
            for (CompletableFuture<Boolean> future : futures) {
                if (!future.get()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateTokenSize(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new InternalServerException(ErrorCode.FCM_NOT_FOUND);
        }
    }

    public void sendMessages(List<Message> messages, FCMChannel channel) {
        try {
            BatchResponse response = firebaseMessaging.sendAll(messages);
            checkAllSuccess(response, channel);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
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
        List<SendResponse> failSend = batchResponse.getResponses().stream()
            .filter(sendResponse -> !sendResponse.isSuccessful())
            .toList();

        if (failSend.isEmpty()) {
            return;
        }

        log.warn("[FCM: {}] 다음 요청들이 실패했습니다. {}", channel, failSend);
        throw new InternalServerException(ErrorCode.FAIL_SEND_FCM_MESSAGE);
    }
}
