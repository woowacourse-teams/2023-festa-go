package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Ticket {

    private static final long ENTRY_LIMIT_HOUR = 24;
    private static final int MIN_TOTAL_AMOUNT = 1;
    private static final long EARLY_ENTRY_LIMIT = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private Integer totalAmount;

    private int reservedAmount = 0;

    private LocalDateTime entryTime;

    protected Ticket() {
    }

    public Ticket(Stage stage, TicketType ticketType, Integer totalAmount, LocalDateTime entryTime) {
        this(null, stage, ticketType, totalAmount, entryTime);
    }

    public Ticket(Long id, Stage stage, TicketType ticketType, Integer totalAmount, LocalDateTime entryTime) {
        validate(totalAmount, stage, entryTime);
        this.id = id;
        this.stage = stage;
        this.ticketType = ticketType;
        this.totalAmount = totalAmount;
        this.entryTime = entryTime;
    }

    private void validate(Integer totalAmount, Stage stage, LocalDateTime entryTime) {
        if (totalAmount < MIN_TOTAL_AMOUNT) {
            throw new BadRequestException(ErrorCode.INVALID_MIN_TICKET_AMOUNT);
        }
        LocalDateTime stageStartTime = stage.getStartTime();
        validateEntryTime(entryTime, stageStartTime);
    }

    private void validateEntryTime(LocalDateTime entryTime, LocalDateTime stageStartTime) {
        if (entryTime.isAfter(stageStartTime) || entryTime.isEqual(stageStartTime)) {
            throw new BadRequestException(ErrorCode.LATE_TICKET_ENTRY_TIME);
        }
        if (entryTime.isBefore(stageStartTime.minusHours(EARLY_ENTRY_LIMIT))) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_TIME);
        }
    }

    public boolean canEntry(LocalDateTime time) {
        return (time.isEqual(entryTime) || time.isAfter(entryTime))
            && time.isBefore(entryTime.plusHours(ENTRY_LIMIT_HOUR));
    }

    public void increaseReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        reservedAmount++;
    }

    public Long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
