package com.festago.stage.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ValidException;
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
import jakarta.validation.constraints.Size;
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

    @Size(max = 255)
    private String lineUp;

    @NotNull
    private LocalDateTime ticketOpenTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @OneToMany(mappedBy = "stage", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    public Stage(LocalDateTime startTime, String lineUp, LocalDateTime ticketOpenTime, Festival festival) {
        this(null, startTime, lineUp, ticketOpenTime, festival);
    }

    public Stage(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        this(null, startTime, null, ticketOpenTime, festival);
    }

    public Stage(Long id, LocalDateTime startTime, String lineUp, LocalDateTime ticketOpenTime,
                 Festival festival) {
        validate(startTime, lineUp, ticketOpenTime, festival);
        this.id = id;
        this.startTime = startTime;
        this.lineUp = lineUp;
        this.ticketOpenTime = ticketOpenTime;
        this.festival = festival;
    }

    private void validate(LocalDateTime startTime, String lineUp, LocalDateTime ticketOpenTime, Festival festival) {
        validateLineUp(lineUp);
        validateFestival(festival);
        validateTime(startTime, ticketOpenTime, festival);
    }

    private void validateLineUp(String lineUp) {
        Validator.maxLength(lineUp, 255, "lineUp은 50글자를 넘을 수 없습니다.");
    }

    private void validateFestival(Festival festival) {
        Validator.notNull(festival, "festival은 null 값이 될 수 없습니다.");
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        Validator.notNull(startTime, "startTime은 null 값이 될 수 없습니다.");
        Validator.notNull(ticketOpenTime, "ticketOpenTime은 null 값이 될 수 없습니다.");
        if (ticketOpenTime.isAfter(startTime) || ticketOpenTime.isEqual(startTime)) {
            throw new ValidException("티켓 오픈 시간은 공연 시작 이전 이어야 합니다.");
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

    public void changeLineUp(String lineUp) {
        validateLineUp(lineUp);
        this.lineUp = lineUp;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getLineUp() {
        return lineUp;
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
