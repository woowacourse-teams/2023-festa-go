package com.festago.entry_alert.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.entry_alert.domain.EntryAlert;
import com.festago.entry_alert.repository.EntryAlertRepository;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.ticketing.repository.MemberTicketRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryAlertService {

    private static final Logger log = LoggerFactory.getLogger(EntryAlertService.class);

    private final EntryAlertRepository entryAlertRepository;
    private final MemberTicketRepository memberTicketRepository;
    private final MemberFCMRepository memberFCMRepository;
    private final FirebaseMessaging firebaseMessaging;

    public EntryAlertService(EntryAlertRepository entryAlertRepository, MemberTicketRepository memberTicketRepository,
                             MemberFCMRepository memberFCMRepository, FirebaseMessaging firebaseMessaging) {
        this.entryAlertRepository = entryAlertRepository;
        this.memberTicketRepository = memberTicketRepository;
        this.memberFCMRepository = memberFCMRepository;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Async
    public void sendEntryAlert(Long id) {
        // TODO: 비관적락
        EntryAlert entryAlert = entryAlertRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
        List<Long> memberIds = memberTicketRepository.findAllOwnerIdByStageIdAndEntryTime(entryAlert.getStageId(),
            entryAlert.getEntryTime());
        // TODO: 500건씩 잘라서 가져오기.
        List<String> tokens = memberFCMRepository.findAllByMemberIdIn(memberIds).stream()
            .map(MemberFCM::getFcmToken)
            .toList();
        List<Message> messages = createMessages(tokens, FCMChannel.NOT_DEFINED.toString());
        try {
            BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
            checkAllSuccess(batchResponse);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        entryAlertRepository.delete(entryAlert);
    }

    // TODO: fcm 패키지에게 요청 -> 중복 줄이기

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
            .setTitle("입장 알림")
            .setBody("입장 어쩌구저쩌구")
            .setChannelId(channelId)
            .build();
    }

    private void checkAllSuccess(BatchResponse batchResponse) {
        List<SendResponse> failSend = batchResponse.getResponses().stream()
            .filter(sendResponse -> !sendResponse.isSuccessful())
            .toList();

        log.warn("다음 요청들이 실패했습니다. {}", failSend);
        throw new InternalServerException(ErrorCode.FAIL_SEND_FCM_MESSAGE);
    }
}
