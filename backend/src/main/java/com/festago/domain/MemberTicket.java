package com.festago.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class MemberTicket extends BaseTimeEntity {

    private static final long ENTRY_LIMIT_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntryState entryState = EntryState.BEFORE_ENTRY;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    private int number;

    private LocalDateTime entryTime;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    protected MemberTicket() {
    }

    public MemberTicket(Member owner, Stage stage, int number, LocalDateTime entryTime, TicketType ticketType) {
        this(null, owner, stage, number, entryTime, ticketType);
    }

    public MemberTicket(Long id, Member owner, Stage stage, int number, LocalDateTime entryTime,
                        TicketType ticketType) {
        this.id = id;
        this.owner = owner;
        this.stage = stage;
        this.number = number;
        this.entryTime = entryTime;
        this.ticketType = ticketType;
    }

    public void changeState(EntryState originState) {
        if (originState != this.entryState) {
            return;
        }
        this.entryState = findNextState(originState);
    }

    private EntryState findNextState(EntryState entryState) {
        if (entryState == EntryState.AFTER_ENTRY) {
            return EntryState.AWAY;
        }
        return EntryState.AFTER_ENTRY;
    }

    public boolean isOwner(Long memberId) {
        return Objects.equals(owner.getId(), memberId);
    }

    public boolean isBeforeEntry(LocalDateTime currentTime) {
        return currentTime.isBefore(entryTime);
    }

    public boolean canEntry(LocalDateTime currentTime) {
        return (currentTime.isEqual(entryTime) || currentTime.isAfter(entryTime))
            && currentTime.isBefore(entryTime.plusHours(ENTRY_LIMIT_HOUR));
    }

    public Long getId() {
        return id;
    }

    public EntryState getEntryState() {
        return entryState;
    }

    public Member getOwner() {
        return owner;
    }

    public Stage getStage() {
        return stage;
    }

    public int getNumber() {
        return number;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
}
