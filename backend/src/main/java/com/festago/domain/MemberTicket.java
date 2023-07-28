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
public class MemberTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntryState entryState = EntryState.BEFORE_ENTRY;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    private int number;

    private LocalDateTime reservedAt;

    protected MemberTicket() {
    }

    public MemberTicket(Member owner, Ticket ticket, int number, LocalDateTime reservedAt) {
        this(null, owner, ticket, number, reservedAt);
    }

    public MemberTicket(Long id, Member owner, Ticket ticket, int number, LocalDateTime reservedAt) {
        this.id = id;
        this.owner = owner;
        this.ticket = ticket;
        this.number = number;
        this.reservedAt = reservedAt;
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

    public boolean canEntry(LocalDateTime time) {
        return ticket.canEntry(time);
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

    public Ticket getTicket() {
        return ticket;
    }

    public int getNumber() {
        return number;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }
}
