package com.festago.ticketing.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.ReservationSequence;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketReserveInfo;
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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTicket extends BaseTimeEntity {

    private static final long ENTRY_LIMIT_HOUR = 24;
    private static final int MIN_NUMBER_VALUE = 1;

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

    @Min(value = MIN_NUMBER_VALUE)
    private int number;

    @NotNull
    private LocalDateTime entryTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

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

    public static MemberTicket createMemberTicket(Ticket ticket, Member member, ReservationSequence sequence,
                                                  LocalDateTime currentTime) {
        if (ticket.alreadyStart(currentTime)) {
            throw new BadRequestException(ErrorCode.TICKET_CANNOT_RESERVE_STAGE_START);
        }

        TicketReserveInfo ticketReserveInfo = extractTicketInfo(ticket, sequence);
        return new MemberTicket(
            member,
            ticketReserveInfo.stage(),
            ticketReserveInfo.sequence().getValue(),
            ticketReserveInfo.entryTime(),
            ticketReserveInfo.ticketType()
        );
    }

    private static TicketReserveInfo extractTicketInfo(Ticket ticket, ReservationSequence sequence) {
        return ticket.extractTicketInfo(sequence);
    }

    private void validate(Member owner, Stage stage, int number, LocalDateTime entryTime, TicketType ticketType) {
        validateOwner(owner);
        validateStage(stage);
        validateNumber(number);
        validateEntryTime(entryTime);
        validateTicketType(ticketType);
    }

    private void validateOwner(Member owner) {
        Validator.notNull(owner, "owner");
    }

    private void validateStage(Stage stage) {
        Validator.notNull(stage, "stage");
    }

    private void validateNumber(int number) {
        Validator.minValue(number, MIN_NUMBER_VALUE, "number");
    }

    private void validateEntryTime(LocalDateTime entryTime) {
        Validator.notNull(entryTime, "entryTime");
    }

    private void validateTicketType(TicketType ticketType) {
        Validator.notNull(ticketType, "ticketType");
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
