package com.festago.ticket.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortNatural;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseTimeEntity {

    private static final int EARLY_ENTRY_LIMIT = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @NotNull
    private Long schoolId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar")
    private TicketType ticketType;

    @OneToOne(mappedBy = "ticket", optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TicketAmount ticketAmount;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ticket_id")
    @SortNatural
    private SortedSet<TicketEntryTime> ticketEntryTimes = new TreeSet<>();

    public Ticket(Stage stage, TicketType ticketType, School school) {
        this(null, stage, ticketType, school);
    }

    public Ticket(Stage stage, TicketType ticketType, Long schoolId) {
        this(null, stage, ticketType, schoolId);
    }

    public Ticket(Long id, Stage stage, TicketType ticketType, School school) {
        this(id, stage, ticketType, school.getId());
    }

    public Ticket(Long id, Stage stage, TicketType ticketType, Long schoolId) {
        validate(stage, ticketType, schoolId);
        this.id = id;
        this.stage = stage;
        this.ticketType = ticketType;
        this.ticketAmount = new TicketAmount(this);
        this.schoolId = schoolId;
    }

    private void validate(Stage stage, TicketType ticketType, Long schoolId) {
        validateStage(stage);
        validateTicketType(ticketType);
        validateSchoolId(schoolId);
    }

    private void validateStage(Stage stage) {
        Validator.notNull(stage, "stage");
    }

    private void validateTicketType(TicketType ticketType) {
        Validator.notNull(ticketType, "ticketType");
    }

    private void validateSchoolId(Long schoolId) {
        Validator.notNull(schoolId, "schoolId");
    }

    public void addTicketEntryTime(LocalDateTime currentTime, LocalDateTime entryTime, int amount) {
        validateEntryTime(currentTime, entryTime);
        TicketEntryTime ticketEntryTime = new TicketEntryTime(entryTime, amount);
        ticketAmount.addTotalAmount(amount);
        ticketEntryTimes.add(ticketEntryTime);
    }

    private void validateEntryTime(LocalDateTime currentTime, LocalDateTime entryTime) {
        LocalDateTime stageStartTime = stage.getStartTime();
        LocalDateTime ticketOpenTime = stage.getTicketOpenTime();
        if (currentTime.isEqual(ticketOpenTime) || currentTime.isAfter(ticketOpenTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_CREATE_TIME);
        }
        if (entryTime.isBefore(ticketOpenTime) || entryTime.isEqual(ticketOpenTime)) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_THAN_OPEN);
        }
        if (entryTime.isAfter(stageStartTime) || entryTime.isEqual(stageStartTime)) {
            throw new BadRequestException(ErrorCode.LATE_TICKET_ENTRY_TIME);
        }
        if (entryTime.isBefore(stageStartTime.minusHours(EARLY_ENTRY_LIMIT))) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_TIME);
        }
    }

    public TicketReserveInfo extractTicketInfo(ReservationSequence sequence) {
        LocalDateTime entryTime = calculateEntryTime(sequence);
        return new TicketReserveInfo(stage,
            sequence,
            entryTime,
            ticketType);
    }

    private LocalDateTime calculateEntryTime(ReservationSequence sequence) {
        int lastSequence = 0;
        int sequenceValue = sequence.getValue();
        for (TicketEntryTime ticketEntryTime : ticketEntryTimes) {
            lastSequence += ticketEntryTime.getAmount();
            if (sequenceValue <= lastSequence) {
                return ticketEntryTime.getEntryTime();
            }
        }
        throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
    }

    public boolean alreadyStart(LocalDateTime currentTime) {
        return stage.isStart(currentTime);
    }

    public Long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketAmount getTicketAmount() {
        return ticketAmount;
    }

    public Set<TicketEntryTime> getTicketEntryTimes() {
        return ticketEntryTimes;
    }
}
