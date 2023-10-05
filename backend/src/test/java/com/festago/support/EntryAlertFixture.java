package com.festago.support;

import com.festago.entryalert.domain.AlertStatus;
import com.festago.entryalert.domain.EntryAlert;
import java.time.LocalDateTime;

public class EntryAlertFixture {

    private Long id;
    private Long stageId = 1L;
    private LocalDateTime entryTime = LocalDateTime.now().plusMinutes(15);
    private AlertStatus status = AlertStatus.PENDING;

    public static EntryAlertFixture entryAlert() {
        return new EntryAlertFixture();
    }

    public EntryAlertFixture id(Long id) {
        this.id = id;
        return this;
    }

    public EntryAlertFixture stageId(Long stageId) {
        this.stageId = stageId;
        return this;
    }

    public EntryAlertFixture entryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public EntryAlertFixture status(AlertStatus status) {
        this.status = status;
        return this;
    }

    public EntryAlert build() {
        EntryAlert entryAlert = new EntryAlert(id, stageId, entryTime);
        if (status != AlertStatus.PENDING) {
            entryAlert.changeRequested();
        }
        return entryAlert;
    }
}
