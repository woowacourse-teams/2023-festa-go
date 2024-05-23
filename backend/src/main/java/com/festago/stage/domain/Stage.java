package com.festago.stage.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.festival.domain.Festival;
import com.festago.ticket.domain.Ticket;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime ticketOpenTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @OneToMany(mappedBy = "stage", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stageId")
    private List<StageArtist> artists = new ArrayList<>();

    public Stage(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        this(null, startTime, ticketOpenTime, festival);
    }

    public Stage(Long id, LocalDateTime startTime, LocalDateTime ticketOpenTime,
                 Festival festival) {
        validate(startTime, ticketOpenTime, festival);
        this.id = id;
        this.startTime = startTime;
        this.ticketOpenTime = ticketOpenTime;
        this.festival = festival;
    }

    private void validate(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        validateFestival(festival);
        validateTime(startTime, ticketOpenTime, festival);
    }

    private void validateFestival(Festival festival) {
        Validator.notNull(festival, "festival");
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        Validator.notNull(startTime, "startTime");
        Validator.notNull(ticketOpenTime, "ticketOpenTime");
        if (ticketOpenTime.isAfter(startTime) || ticketOpenTime.isEqual(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_OPEN_TIME);
        }
        if (festival.isNotInDuration(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_STAGE_START_TIME);
        }
    }

    public boolean isStart(LocalDateTime currentTime) {
        return currentTime.isAfter(startTime);
    }

    public void changeTime(LocalDateTime startTime, LocalDateTime ticketOpenTime) {
        validateTime(startTime, ticketOpenTime, this.festival);
        this.startTime = startTime;
        this.ticketOpenTime = ticketOpenTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getTicketOpenTime() {
        return ticketOpenTime;
    }

    public Festival getFestival() {
        return festival;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}
