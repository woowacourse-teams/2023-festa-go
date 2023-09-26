package com.festago.fcm.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.festago.common.exception.InternalServerException;
import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.fcm.dto.MemberFCMResponse;
import com.festago.fcm.dto.MemberFCMsResponse;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.SendResponse;
import java.util.List;
import org.assertj.core.api.Assertions;
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
    void 유저의_FCM_요청_중_하나라도_실패하면_예외() throws FirebaseMessagingException {
        // given
        given(memberFCMService.findMemberFCM(anyLong())).willReturn(
            new MemberFCMsResponse(List.of(new MemberFCMResponse(1L, 1L, "token1"), new MemberFCMResponse(2L, 1L, "token2"))));

        given(firebaseMessaging.sendAll(any())).willReturn(new MockBatchResponse());

        EntryProcessEvent event = new EntryProcessEvent(1L);

        // when & then
        Assertions.assertThatThrownBy(() -> FCMNotificationEventListener.sendFcmNotification(event))
            .isInstanceOf(InternalServerException.class);
    }

    private static class MockBatchResponse implements BatchResponse {

        @Override
        public List<SendResponse> getResponses() {
            SendResponse mockResponse = mock(SendResponse.class);
            when(mockResponse.isSuccessful()).thenReturn(false);
            return List.of(mockResponse, mockResponse);
        }

        @Override
        public int getSuccessCount() {
            return 0;
        }

        @Override
        public int getFailureCount() {
            return 0;
        }
    }
}
