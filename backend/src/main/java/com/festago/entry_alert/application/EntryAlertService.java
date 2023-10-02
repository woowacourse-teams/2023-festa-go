package com.festago.entry_alert.application;

import com.festago.entry_alert.domain.EntryAlert;
import com.festago.entry_alert.repository.EntryAlertRepository;
import com.festago.fcm.application.FcmClient;
import com.festago.fcm.domain.FCMChannel;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.FcmPayload;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.ticketing.repository.MemberTicketRepository;
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
    private final FcmClient fcmClient;

    public EntryAlertService(EntryAlertRepository entryAlertRepository, MemberTicketRepository memberTicketRepository,
                             MemberFCMRepository memberFCMRepository, FcmClient fcmClient) {
        this.entryAlertRepository = entryAlertRepository;
        this.memberTicketRepository = memberTicketRepository;
        this.memberFCMRepository = memberFCMRepository;
        this.fcmClient = fcmClient;
    }

    @Async
    public void sendEntryAlert(Long id) {
        // TODO: 비관적락
        EntryAlert entryAlert = entryAlertRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
        List<Long> memberIds = memberTicketRepository.findAllOwnerIdByStageIdAndEntryTime(entryAlert.getStageId(),
            entryAlert.getEntryTime());
        // TODO: 500건씩 잘라서 가져오기.
        List<String> tokens = memberFCMRepository.findAllByMemberIdIn(memberIds)
            .stream()
            .map(MemberFCM::getFcmToken)
            .toList();
        fcmClient.sendAll(tokens, FCMChannel.ENTRY_ALERT, FcmPayload.entryAlert());
        entryAlertRepository.delete(entryAlert);
    }
}
