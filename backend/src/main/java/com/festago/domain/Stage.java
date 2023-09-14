package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
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

@Entity
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

    protected Stage() {
    }

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
        checkNotNull(startTime, ticketOpenTime, festival);
        checkLength(lineUp);
        validateTime(startTime, ticketOpenTime, festival);
    }

    private void checkNotNull(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        if (startTime == null ||
            ticketOpenTime == null ||
            festival == null) {
            throw new IllegalArgumentException("Stage 는 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    private void checkLength(String lineUp) {
        if (overLength(lineUp, 255)) {
            throw new IllegalArgumentException("Stage 의 필드로 허용된 범위를 넘은 column 을 넣을 수 없습니다.");
        }
    }

    private boolean overLength(String target, int maxLength) {
        if (target == null) {
            return false;
        }
        return target.length() > maxLength;
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        if (festival.isNotInDuration(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_STAGE_START_TIME);
        }
        if (ticketOpenTime.isAfter(startTime) || ticketOpenTime.isEqual(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_OPEN_TIME);
        }
    }

    public boolean isStart(LocalDateTime currentTime) {
        return currentTime.isAfter(startTime);
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
