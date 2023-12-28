package com.festago.ticket.domain;

import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketInfo {

    private final Stage stage;
    private final int reservationSequence;
    private final LocalDateTime entryTime;
    private final TicketType ticketType;

    public Stage getStage() {
        return stage;
    }

    public int getReservationSequence() {
        return reservationSequence;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
}
