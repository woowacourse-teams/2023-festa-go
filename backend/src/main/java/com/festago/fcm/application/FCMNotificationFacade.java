package com.festago.fcm.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.entry.dto.EntryProcessEvent;
import com.festago.fcm.dto.MemberFCMResponse;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class FCMNotificationFacade {

    private static final Logger logger = LoggerFactory.getLogger(FCMNotificationFacade.class);

    private final FirebaseMessaging firebaseMessaging;

    private final MemberFCMService memberFCMService;

    public FCMNotificationFacade(FirebaseMessaging firebaseMessaging, MemberFCMService memberFCMService) {
        this.firebaseMessaging = firebaseMessaging;
        this.memberFCMService = memberFCMService;
    }

    @TransactionalEventListener
    @Async
    public void receiveEvent(EntryProcessEvent event) {
        // TODO: channelId로 이벤트 구분 하는 로직 필요
        List<Message> messages = createMessages(getMemberFCMToken(event.memberId()), null);
        try {
            BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
            checkAllSuccess(batchResponse, event.memberId());
        } catch (FirebaseMessagingException e) {
            logger.error("fail send FCM message", e);
            throw new InternalServerException(ErrorCode.FAIL_SEND_FCM_MESSAGE);
        }
    }

    private List<String> getMemberFCMToken(Long memberId) {
        return memberFCMService.findMemberFCM(memberId).memberFCMs().stream()
            .map(MemberFCMResponse::fcmToken)
            .toList();
    }

    private List<Message> createMessages(List<String> tokens, String channelId) {
        return tokens.stream()
            .map(token -> createMessage(token, channelId))
            .toList();
    }

    private Message createMessage(String token, String channelId) {
        return Message.builder()
            .setAndroidConfig(createAndroidConfig(channelId))
            .setToken(token)
            .build();
    }

    private AndroidConfig createAndroidConfig(String channelId) {
        return AndroidConfig.builder()
            .setNotification(createAndroidNotification(channelId))
            .build();
    }

    private AndroidNotification createAndroidNotification(String channelId) {
        return AndroidNotification.builder()
            .setChannelId(channelId)
            .build();
    }

    private void checkAllSuccess(BatchResponse batchResponse, Long memberid) {
        List<SendResponse> failSend = batchResponse.getResponses().stream()
            .filter(sendResponse -> !sendResponse.isSuccessful())
            .toList();

        logger.warn("member {} 에 대한 다음 요청들이 실패했습니다. {}", memberid, failSend);
        throw new InternalServerException(ErrorCode.FAIL_SEND_FCM_MESSAGE);
    }
}
