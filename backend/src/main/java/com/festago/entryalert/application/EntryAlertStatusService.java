package com.festago.entryalert.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.entryalert.domain.EntryAlert;
import com.festago.entryalert.repository.EntryAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntryAlertStatusService {

    private final EntryAlertRepository entryAlertRepository;

    public EntryAlertStatusService(EntryAlertRepository entryAlertRepository) {
        this.entryAlertRepository = entryAlertRepository;
    }

    @Transactional
    public void updateRequestedEntryAlertStatus(Long entryAlertId, boolean isSuccess) {
        EntryAlert entryAlert = entryAlertRepository.findById(entryAlertId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ENTRY_ALERT_NOT_FOUND));
        if (isSuccess) {
            entryAlert.send();
            return;
        }
        entryAlert.fail();
    }
}
