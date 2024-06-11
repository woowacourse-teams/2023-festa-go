package com.festago.ticket.domain;

import com.festago.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StageTicketEntryTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stageTicketId;

    private LocalDateTime entryTime;

    private int amount;

    public StageTicketEntryTime(Long stageTicketId, LocalDateTime entryTime, int amount) {
        this(null, stageTicketId, entryTime, amount);
    }

    public StageTicketEntryTime(Long id, Long stageTicketId, LocalDateTime entryTime, int amount) {
        this.id = id;
        this.stageTicketId = stageTicketId;
        this.entryTime = entryTime;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getStageTicketId() {
        return stageTicketId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public int getAmount() {
        return amount;
    }
}
