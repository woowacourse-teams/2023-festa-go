package com.festago.entryalert.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class EntryAlert extends BaseTimeEntity {

    private static final int ENTRY_ALERT_MINUTES_BEFORE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long stageId;

    @NotNull
    private LocalDateTime entryTime;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private AlertStatus status = AlertStatus.PENDING;

    protected EntryAlert() {
    }

    public EntryAlert(Long stageId, LocalDateTime entryTime) {
        this(null, stageId, entryTime);
    }

    public EntryAlert(Long id, Long stageId, LocalDateTime entryTime) {
        this.id = id;
        this.stageId = stageId;
        this.entryTime = entryTime;
    }

    public static EntryAlert create(Long stageId, LocalDateTime entryTime, LocalDateTime currentTime) {
        if (currentTime.isAfter(entryTime.minusMinutes(ENTRY_ALERT_MINUTES_BEFORE))) {
            throw new BadRequestException(ErrorCode.INVALID_ENTRY_ALERT_TIME);
        }
        return new EntryAlert(stageId, entryTime);
    }

    public void request() {
        validateNotPending();
        this.status = AlertStatus.REQUESTED;
    }

    private void validateNotPending() {
        if (status != AlertStatus.PENDING) {
            throw new BadRequestException(ErrorCode.NOT_PENDING_ALERT);
        }
    }

    public LocalDateTime findAlertTime() {
        return entryTime.minusMinutes(ENTRY_ALERT_MINUTES_BEFORE);
    }

    public Long getId() {
        return id;
    }

    public Long getStageId() {
        return stageId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public AlertStatus getStatus() {
        return status;
    }
}
