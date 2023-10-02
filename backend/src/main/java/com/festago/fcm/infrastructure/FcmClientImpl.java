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
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FcmClientImpl implements FcmClient {

    private static final Logger log = LoggerFactory.getLogger(FcmClientImpl.class);

    private final FirebaseMessaging firebaseMessaging;

    public FcmClientImpl(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendAll(List<String> tokens, FCMChannel channel) {
        sendAll(tokens, channel, FcmPayload.empty());
    }

    @Override
    public void sendAll(List<String> tokens, FCMChannel channel, FcmPayload payload) {
        List<Message> messages = createMessages(tokens, channel, payload);
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
