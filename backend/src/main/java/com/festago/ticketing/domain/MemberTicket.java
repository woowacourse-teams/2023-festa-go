package com.festago.ticketing.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.TicketType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class MemberTicket extends BaseTimeEntity {

    private static final long ENTRY_LIMIT_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EntryState entryState = EntryState.BEFORE_ENTRY;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @Min(value = 0)
    private int number;

    @NotNull
    private LocalDateTime entryTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    protected MemberTicket() {
    }

    public MemberTicket(Member owner, Stage stage, int number, LocalDateTime entryTime, TicketType ticketType) {
        this(null, owner, stage, number, entryTime, ticketType);
    }

    public MemberTicket(Long id, Member owner, Stage stage, int number, LocalDateTime entryTime,
                        TicketType ticketType) {
        validate(owner, stage, number, entryTime, ticketType);
        this.id = id;
        this.owner = owner;
        this.stage = stage;
        this.number = number;
        this.entryTime = entryTime;
        this.ticketType = ticketType;
    }

    private void validate(Member owner, Stage stage, int number, LocalDateTime entryTime, TicketType ticketType) {
        checkNotNull(owner, stage, entryTime, ticketType);
        checkScope(number);
    }

    private void checkNotNull(Member owner, Stage stage, LocalDateTime entryTime, TicketType ticketType) {
        if (owner == null ||
            stage == null ||
            entryTime == null ||
            ticketType == null) {
            throw new IllegalArgumentException("MemberTicket 은 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    private void checkScope(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("MemberTicket 의 필드로 허용된 범위를 넘은 column 을 넣을 수 없습니다.");
        }
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
