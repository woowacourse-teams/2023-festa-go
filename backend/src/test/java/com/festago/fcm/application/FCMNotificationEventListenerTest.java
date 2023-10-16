package com.festago.fcm.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.fcm.dto.MemberFCMResponse;
import com.festago.fcm.dto.MemberFCMsResponse;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.SendResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class FCMNotificationEventListenerTest {

    @Mock
    FirebaseMessaging firebaseMessaging;

    @Mock
    MemberFCMService memberFCMService;

    @InjectMocks
    FCMNotificationEventListener FCMNotificationEventListener;

    @Test
    void 유저의_모든_FCM_요청이_성공() throws FirebaseMessagingException {
        // given
        given(memberFCMService.findMemberFCM(anyLong())).willReturn(
            new MemberFCMsResponse(List.of(new MemberFCMResponse(1L, 1L, "token1"),
                new MemberFCMResponse(2L, 1L, "token2"))));
        BatchResponse mockBatchResponse = mock(BatchResponse.class);
        given(mockBatchResponse.getFailureCount())
            .willReturn(0);

        given(firebaseMessaging.sendAll(any())).willReturn(mockBatchResponse);
        EntryProcessEvent event = new EntryProcessEvent(1L);

        // when
        FCMNotificationEventListener.sendFcmNotification(event);

        // then
        verify(mockBatchResponse, times(1))
            .getFailureCount();
        verify(mockBatchResponse, never())
            .getResponses();
    }

    @Test
    void 유저의_FCM_요청_중_하나라도_실패하면_예외() throws FirebaseMessagingException {
        // given
        given(memberFCMService.findMemberFCM(anyLong())).willReturn(
            new MemberFCMsResponse(List.of(new MemberFCMResponse(1L, 1L, "token1"),
                new MemberFCMResponse(2L, 1L, "token2"))));

        BatchResponse mockBatchResponse = mock(BatchResponse.class);
        SendResponse mockSendResponse = mock(SendResponse.class);

        given(mockSendResponse.isSuccessful())
            .willReturn(false);
        given(mockBatchResponse.getFailureCount())
            .willReturn(1);
        given(mockBatchResponse.getResponses())
            .willReturn(List.of(mockSendResponse));

        given(firebaseMessaging.sendAll(any())).willReturn(mockBatchResponse);

        EntryProcessEvent event = new EntryProcessEvent(1L);

        // when
        FCMNotificationEventListener.sendFcmNotification(event);

        // then
        verify(mockBatchResponse, times(1))
            .getFailureCount();
        verify(mockBatchResponse, times(1))
            .getResponses();
        verify(mockSendResponse, times(1))
            .isSuccessful();
    }
}
