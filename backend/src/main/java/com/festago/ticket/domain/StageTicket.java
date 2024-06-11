package com.festago.ticket.domain;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.common.util.Validator;
import com.festago.stage.domain.Stage;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.ReserveTicket;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("STAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StageTicket extends NewTicket {

    private static final int EARLY_ENTRY_LIMIT = 12;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @Embedded
    private StageTicketEntryTimes ticketEntryTimes = new StageTicketEntryTimes();

    public StageTicket(Long schoolId, TicketExclusive ticketType, Stage stage) {
        this(null, schoolId, ticketType, stage);
    }

    public StageTicket(Long id, Long schoolId, TicketExclusive ticketType, Stage stage) {
        super(id, schoolId, ticketType);
        validate(schoolId, stage);
        this.stage = stage;
    }

    private void validate(Long schoolId, Stage stage) {
        Validator.notNull(stage, "stage");
        if (!stage.isSchoolStage(schoolId)) {
            throw new UnauthorizedException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
    }

    @Override
    public LocalDateTime getTicketingEndTime() {
        return stage.getStartTime();
    }

    @Override
    public void validateReserve(Booker booker, LocalDateTime currentTime) {
        if (isStudentOnly() && !isSchoolStudent(booker)) {
            throw new BadRequestException(ErrorCode.RESERVE_TICKET_NOT_SCHOOL_STUDENT);
        }
        if (stage.isStart(currentTime)) {
            throw new BadRequestException(ErrorCode.TICKET_CANNOT_RESERVE_STAGE_START);
        }
        if (stage.isBeforeTicketOpenTime(currentTime)) {
            throw new BadRequestException(ErrorCode.RESERVE_TICKET_BEFORE_TICKET_OPEN_TIME);
        }
    }

    @Override
    public ReserveTicket reserve(Booker booker, int sequence) {
        LocalDateTime entryTime = ticketEntryTimes.calculateEntryTime(sequence);
        return new ReserveTicket(booker.getMemberId(), NewTicketType.STAGE, id, sequence, entryTime);
    }

    public void addTicketEntryTime(Long schoolId, LocalDateTime currentTime, LocalDateTime entryTime, int amount) {
        validateSchoolOwner(schoolId);
        validateEntryTime(currentTime, entryTime);
        ticketEntryTimes.add(new StageTicketEntryTime(id, entryTime, amount));
        changeAmount(ticketEntryTimes.getTotalAmount());
    }

    private void validateSchoolOwner(Long schoolId) {
        if (!Objects.equals(this.schoolId, schoolId)) {
            throw new UnauthorizedException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
    }

    private void validateEntryTime(LocalDateTime currentTime, LocalDateTime entryTime) {
        if (!stage.isBeforeTicketOpenTime(currentTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_CREATE_TIME);
        }
        if (stage.isBeforeTicketOpenTime(entryTime)) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_THAN_OPEN);
        }
        if (stage.isStart(entryTime)) {
            throw new BadRequestException(ErrorCode.LATE_TICKET_ENTRY_TIME);
        }
        if (!stage.isStart(entryTime.plusHours(EARLY_ENTRY_LIMIT))) {
            throw new BadRequestException(ErrorCode.EARLY_TICKET_ENTRY_TIME);
        }
    }

    public boolean deleteTicketEntryTime(Long schoolId, LocalDateTime currentTime, LocalDateTime entryTime) {
        validateSchoolOwner(schoolId);
        if (!stage.isBeforeTicketOpenTime(currentTime)) {
            throw new BadRequestException(ErrorCode.STAGE_TICKET_DELETE_CONSTRAINT_TICKET_OPEN_TIME);
        }
        boolean isDeleted = ticketEntryTimes.remove(entryTime);
        if (isDeleted) {
            changeAmount(ticketEntryTimes.getTotalAmount());
        }
        return isDeleted;
    }

    public Stage getStage() {
        return stage;
    }
}
