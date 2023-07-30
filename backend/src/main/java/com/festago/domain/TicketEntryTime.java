package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class TicketEntryTime implements Comparable<TicketEntryTime> {

    private static final long EARLY_ENTRY_LIMIT = 12;
    private static final int MIN_TOTAL_AMOUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entryTime;

    private Integer amount;

    protected TicketEntryTime() {
    }

    private TicketEntryTime(LocalDateTime entryTime, Integer amount) {
        this(null, entryTime, amount);
    }

    private TicketEntryTime(Long id, LocalDateTime entryTime, Integer amount) {
        validate(amount);
        this.id = id;
        this.entryTime = entryTime;
        this.amount = amount;
    }

    private void validate(Integer amount) {
        if (amount < MIN_TOTAL_AMOUNT) {
            throw new BadRequestException(ErrorCode.INVALID_MIN_TICKET_AMOUNT);
        }
    }

    public static TicketEntryTime of(LocalDateTime entryTime, Integer amount, Stage stage) {
        validateEntryTime(entryTime, stage);
        return new TicketEntryTime(entryTime, amount);
    }

    private static void validateEntryTime(LocalDateTime entryTime, Stage stage) {
        LocalDateTime stageStartTime = stage.getStartTime();
        if (entryTime.isAfter(stageStartTime) || entryTime.isEqual(stageStartTime)) {
            throw new BadRequestException(ErrorCode.LATE_TICKET_ENTRY_TIME);
        }
        if (entryTime.isBefore(stageStartTime.minusHours(EARLY_ENTRY_LIMIT))) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_TIME);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public int compareTo(TicketEntryTime o) {
        return entryTime.compareTo(o.getEntryTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketEntryTime that)) {
            return false;
        }

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
