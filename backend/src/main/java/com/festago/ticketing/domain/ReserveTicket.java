package com.festago.ticketing.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import com.festago.ticket.domain.NewTicketType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReserveTicket extends BaseTimeEntity {

    private static final long ENTRY_LIMIT_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar")
    private EntryState entryState = EntryState.BEFORE_ENTRY;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar")
    private NewTicketType ticketType;

    private Long ticketId;

    private int sequence;

    private LocalDateTime entryTime;

    public ReserveTicket(Long memberId, NewTicketType ticketType, Long ticketId, int sequence,
                         LocalDateTime entryTime) {
        this(null, memberId, ticketType, ticketId, sequence, entryTime);
    }

    public ReserveTicket(Long id, Long memberId, NewTicketType ticketType, Long ticketId, int sequence,
                         LocalDateTime entryTime) {
        Validator.notNull(memberId, "memberId");
        Validator.notNull(ticketId, "ticketId");
        Validator.minValue(sequence, 1, "sequence");
        Validator.notNull(entryTime, "entryTime");
        Validator.notNull(ticketType, "ticketType");
        this.id = id;
        this.memberId = memberId;
        this.ticketType = ticketType;
        this.ticketId = ticketId;
        this.sequence = sequence;
        this.entryTime = entryTime;
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
        return Objects.equals(this.memberId, memberId);
    }

    public boolean canEntry(LocalDateTime currentTime) {
        return !isBeforeEntry(currentTime) && currentTime.isBefore(entryTime.plusHours(ENTRY_LIMIT_HOUR));
    }

    public boolean isBeforeEntry(LocalDateTime currentTime) {
        return currentTime.isBefore(entryTime);
    }

    public Long getId() {
        return id;
    }

    public EntryState getEntryState() {
        return entryState;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public int getSequence() {
        return sequence;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public NewTicketType getTicketType() {
        return ticketType;
    }
}
