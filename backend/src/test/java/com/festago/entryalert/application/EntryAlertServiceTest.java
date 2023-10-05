package com.festago.entryalert.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyList;

import com.festago.entryalert.domain.AlertStatus;
import com.festago.entryalert.domain.EntryAlert;
import com.festago.entryalert.repository.EntryAlertRepository;
import com.festago.fcm.application.FcmClient;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.support.EntryAlertFixture;
import com.festago.support.SetUpMockito;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class EntryAlertServiceTest {

    @Mock
    EntryAlertRepository entryAlertRepository;

    @Mock
    MemberTicketRepository memberTicketRepository;

    @Mock
    MemberFCMRepository memberFCMRepository;

    @Mock
    FcmClient fcmClient;

    @Mock
    TaskExecutor taskExecutor;

    @InjectMocks
    EntryAlertService entryAlertService;

    @Nested
    class 알림_전송 {

        Long id;
        EntryAlert entryAlert;

        @BeforeEach
        void setUp() {
            id = 1L;
            entryAlert = EntryAlertFixture.entryAlert().id(id).status(AlertStatus.PENDING).build();

            SetUpMockito
                .given(entryAlertRepository.findByIdAndStatusForUpdate(id, AlertStatus.PENDING))
                .willReturn(Optional.of(entryAlert));

            SetUpMockito
                .given(memberTicketRepository.findAllOwnerIdByStageIdAndEntryTime(entryAlert.getStageId(),
                    entryAlert.getEntryTime()))
                .willReturn(List.of(1L, 2L, 3L));

            SetUpMockito
                .given(memberFCMRepository.findAllTokenByMemberIdIn(anyList()))
                .willReturn(List.of("token1", "token2", "token3"));
        }

        @Test
        void 성공() {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> entryAlertService.sendEntryAlert(id));
        }
    }
}
