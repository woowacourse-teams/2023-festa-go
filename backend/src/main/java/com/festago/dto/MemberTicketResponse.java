package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.Festival;
import com.festago.domain.MemberTicket;
import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import java.time.LocalDateTime;

public record MemberTicketResponse(Long id, Integer number, LocalDateTime entryTime, EntryState state,
                                   LocalDateTime reservedAt, StageResponse stage,
                                   MemberTicketFestivalResponse festival) {

    // TODO: FestivalResponse
    public static MemberTicketResponse from(MemberTicket memberTicket) {
        Ticket ticket = memberTicket.getTicket();
        Stage stage = ticket.getStage();
        Festival festival = stage.getFestival();

        return new MemberTicketResponse(
            memberTicket.getId(),
            memberTicket.getNumber(),
            ticket.getEntryTime(),
            memberTicket.getEntryState(),
            memberTicket.getReservedAt(),
            StageResponse.from(stage),
            MemberTicketFestivalResponse.from(festival));
    }
}
