package com.festago.entry_alert.application;

import com.festago.common.exception.ConflictException;
import com.festago.common.exception.ErrorCode;
import com.festago.entry_alert.domain.EntryAlert;
import com.festago.entry_alert.dto.EntryAlertResponse;
import com.festago.entry_alert.repository.EntryAlertRepository;
import com.festago.fcm.application.FcmClient;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.FcmPayload;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryAlertService {

    private static final int BATCH_ALERT_SIZE = 500;

    private final EntryAlertRepository entryAlertRepository;
    private final MemberTicketRepository memberTicketRepository;
    private final MemberFCMRepository memberFCMRepository;
    private final FcmClient fcmClient;

    public EntryAlertService(EntryAlertRepository entryAlertRepository, MemberTicketRepository memberTicketRepository,
                             MemberFCMRepository memberFCMRepository, FcmClient fcmClient) {
        this.entryAlertRepository = entryAlertRepository;
        this.memberTicketRepository = memberTicketRepository;
        this.memberFCMRepository = memberFCMRepository;
        this.fcmClient = fcmClient;
    }

    @Transactional(readOnly = true)
    public List<EntryAlertResponse> findAll() {
        return entryAlertRepository.findAll().stream()
            .map(alert -> new EntryAlertResponse(alert.getId(), alert.findAlertTime()))
            .toList();
    }

    public EntryAlertResponse create(Long stageId, LocalDateTime entryTime) {
        EntryAlert entryAlert = entryAlertRepository.save(new EntryAlert(stageId, entryTime));
        return new EntryAlertResponse(entryAlert.getId(), entryAlert.findAlertTime());
    }

    @Async
    public void sendEntryAlert(Long id) {
        EntryAlert entryAlert = entryAlertRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ConflictException(ErrorCode.ALREADY_ALERT));
        List<Long> memberIds = memberTicketRepository.findAllOwnerIdByStageIdAndEntryTime(entryAlert.getStageId(),
            entryAlert.getEntryTime());
        List<String> tokens = memberFCMRepository.findAllByMemberIdIn(memberIds)
            .stream()
            .map(MemberFCM::getFcmToken)
            .toList();
        sendMessages(tokens);
        entryAlertRepository.delete(entryAlert);
    }

    private void sendMessages(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i += BATCH_ALERT_SIZE) {
            List<String> subTokens = tokens.subList(i, Math.min(i + BATCH_ALERT_SIZE, tokens.size()));
            fcmClient.sendAll(subTokens, FCMChannel.ENTRY_ALERT, FcmPayload.entryAlert());
        }
    }
}
