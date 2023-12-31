package com.festago.ticket.domain;

import com.festago.common.util.Validator;
import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

public class TicketInfo {

    private final Stage stage;
    private final ReservationSequence sequence;
    private final LocalDateTime entryTime;
    private final TicketType ticketType;

    public TicketInfo(Stage stage, ReservationSequence sequence, LocalDateTime entryTime,
                      TicketType ticketType) {
        validate(stage, sequence, entryTime, ticketType);
        this.stage = stage;
        this.sequence = sequence;
        this.entryTime = entryTime;
        this.ticketType = ticketType;
    }

    private void validate(Stage stage, ReservationSequence sequence, LocalDateTime entryTime, TicketType ticketType) {
        validateStage(stage);
        validateSequence(sequence);
        validateEntryTime(entryTime);
        validateTicketType(ticketType);
    }

    private void validateStage(Stage stage) {
        Validator.notNull(stage, "stage");
    }

    private void validateSequence(ReservationSequence sequence) {
        Validator.notNull(sequence, "sequence");
    }

    private void validateEntryTime(LocalDateTime entryTime) {
        Validator.notNull(entryTime, "entryTime");
    }

    private void validateTicketType(TicketType ticketType) {
        Validator.notNull(ticketType, "ticketType");
    }

    public Stage getStage() {
        return stage;
    }

    public ReservationSequence getSequence() {
        return sequence;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
}
