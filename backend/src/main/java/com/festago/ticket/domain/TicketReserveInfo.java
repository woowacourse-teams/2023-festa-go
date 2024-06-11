package com.festago.ticket.domain;

import com.festago.common.util.Validator;
import com.festago.stage.domain.Stage;
import java.time.LocalDateTime;

@Deprecated(forRemoval = true)
public record TicketReserveInfo(
    Stage stage,
    ReservationSequence sequence,
    LocalDateTime entryTime,
    TicketType ticketType
) {

    public TicketReserveInfo {
        validate(stage, sequence, entryTime, ticketType);
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
}
