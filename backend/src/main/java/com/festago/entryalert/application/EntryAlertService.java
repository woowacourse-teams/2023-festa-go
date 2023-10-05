package com.festago.entryalert.application;

import com.festago.common.exception.ConflictException;
import com.festago.common.exception.ErrorCode;
import com.festago.entryalert.domain.AlertStatus;
import com.festago.entryalert.domain.EntryAlert;
import com.festago.entryalert.dto.EntryAlertResponse;
import com.festago.entryalert.repository.EntryAlertRepository;
import com.festago.fcm.application.FcmClient;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.dto.FcmPayload;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EntryAlertService {

    private final EntryAlertRepository entryAlertRepository;
    private final MemberTicketRepository memberTicketRepository;
    private final MemberFCMRepository memberFCMRepository;
    private final FcmClient fcmClient;
    private final TaskExecutor taskExecutor;

    @Transactional(readOnly = true)
    public List<EntryAlertResponse> findAllPending() {
        return entryAlertRepository.findAllByStatus(AlertStatus.PENDING)
            .stream()
            .map(alert -> new EntryAlertResponse(alert.getId(), alert.findAlertTime()))
            .toList();
    }

    public EntryAlertResponse create(Long stageId, LocalDateTime entryTime) {
        EntryAlert entryAlert = entryAlertRepository.save(new EntryAlert(stageId, entryTime));
        return new EntryAlertResponse(entryAlert.getId(), entryAlert.findAlertTime());
    }

    @Async
    public void sendEntryAlert(Long id) {
        EntryAlert entryAlert = entryAlertRepository.findByIdAndStatusForUpdate(id, AlertStatus.PENDING)
            .orElseThrow(() -> new ConflictException(ErrorCode.ALREADY_ALERT));
        List<String> tokens = findFcmTokens(entryAlert);
        log.info("EntryAlert 전송 시작 / entryAlertId: {} / to {} devices", id, tokens.size());
        taskExecutor.execute(() -> fcmClient.sendAll(tokens, FCMChannel.ENTRY_ALERT, FcmPayload.entryAlert()));
        entryAlert.changeRequested();
    }

    private List<String> findFcmTokens(EntryAlert entryAlert) {
        List<Long> memberIds = memberTicketRepository.findAllOwnerIdByStageIdAndEntryTime(
            entryAlert.getStageId(), entryAlert.getEntryTime());
        return memberFCMRepository.findAllTokenByMemberIdIn(memberIds);
    }
}
