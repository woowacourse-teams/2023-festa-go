package com.festago.fcm.infrastructure;

import static com.festago.common.exception.ErrorCode.FAIL_SEND_FCM_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.festago.common.exception.InternalServerException;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.SendResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class FcmClientImplTest {

    @Mock
    FirebaseMessaging firebaseMessaging;
    
    @InjectMocks
    FcmClientImpl fcmClient;

    List<String> tokens;
    FCMChannel fcmChannel;
    FcmPayload fcmPayload;

    @BeforeEach
    void setUp() {
        tokens = List.of("token1", "token2");
        fcmChannel = FCMChannel.ENTRY_ALERT;
        fcmPayload = FcmPayload.entryAlert();
    }

    @Test
    void 유저의_FCM_요청_중_하나라도_실패하면_예외() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.sendAll(anyList()))
            .willReturn(new MockBatchResponse(false));

        // when & then
        assertThatThrownBy(() -> fcmClient.sendAll(tokens, fcmChannel, fcmPayload))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(FAIL_SEND_FCM_MESSAGE.getMessage());
    }

    @Test
    void 모두_성공하면_성공() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.sendAll(anyList()))
            .willReturn(new MockBatchResponse(true));

        // when & then
        assertThatNoException()
            .isThrownBy(() -> fcmClient.sendAll(tokens, fcmChannel, fcmPayload));
    }

    private static class MockBatchResponse implements BatchResponse {

        private final boolean isSuccessful;

        private MockBatchResponse(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        @Override
        public List<SendResponse> getResponses() {
            SendResponse mockResponse = mock(SendResponse.class);

            when(mockResponse.isSuccessful())
                .thenReturn(isSuccessful);

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
