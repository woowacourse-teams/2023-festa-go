package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
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

    public Stage(Long id, LocalDateTime startTime, String lineUp, LocalDateTime ticketOpenTime,
                 Festival festival) {
        validate(startTime, ticketOpenTime, festival);
        this.id = id;
        this.startTime = startTime;
        this.lineUp = lineUp;
        this.ticketOpenTime = ticketOpenTime;
        this.festival = festival;
    }

    private void validate(LocalDateTime startTime, LocalDateTime ticketOpenTime, Festival festival) {
        if (festival.isNotInDuration(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_STAGE_START_TIME);
        }
        if (ticketOpenTime.isAfter(startTime) || ticketOpenTime.isEqual(startTime)) {
            throw new BadRequestException(ErrorCode.INVALID_TICKET_OPEN_TIME);
        }
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
