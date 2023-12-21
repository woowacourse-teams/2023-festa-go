package com.festago.fcm.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.entry.dto.event.EntryProcessEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FCMNotificationEventListenerTest {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @MockBean
    FCMNotificationEventListener fcmNotificationEventListener;

    @Test
    @Transactional
    void 이벤트를_발행하고_트랜잭션이_커밋되면_이벤트_수신() {
        // given
        eventPublisher.publishEvent(new EntryProcessEvent(1L));

        // when
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        verify(fcmNotificationEventListener, times(1))
            .sendFcmNotification(any(EntryProcessEvent.class));
    }

    @Test
    @Transactional
    void 이벤트를_발행하고_트랜잭션이_롤백되면_이벤트_수신_하지않음() {
        // given
        eventPublisher.publishEvent(new EntryProcessEvent(1L));

        // when
        TestTransaction.flagForRollback();
        TestTransaction.end();

        // then
        verify(fcmNotificationEventListener, never())
            .sendFcmNotification(any(EntryProcessEvent.class));
    }
}
