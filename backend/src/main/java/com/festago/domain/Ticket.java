package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {

    private static final int EARLY_ENTRY_LIMIT = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TicketAmount ticketAmount = new TicketAmount();

    @OneToMany
    @JoinColumn(name = "ticket_id")
    private List<TicketEntryTime> ticketEntryTimes = new ArrayList<>();

    protected Ticket() {
    }

    public Ticket(Stage stage, TicketType ticketType) {
        this(null, stage, ticketType);
    }

    public Ticket(Long id, Stage stage, TicketType ticketType) {
        this.id = id;
        this.stage = stage;
        this.ticketType = ticketType;
    }

    public void addTicketEntryTime(LocalDateTime entryTime, int amount) {
        validateEntryTime(entryTime);
        TicketEntryTime ticketEntryTime = new TicketEntryTime(entryTime, amount);
        ticketAmount.addTotalAmount(amount);
        ticketEntryTimes.add(ticketEntryTime);
    }

    private void validateEntryTime(LocalDateTime entryTime) {
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

    public Stage getStage() {
        return stage;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketAmount getTicketAmount() {
        return ticketAmount;
    }

    public List<TicketEntryTime> getTicketEntryTimes() {
        return ticketEntryTimes;
    }
}
