package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.MemberTicket;
import com.festago.domain.Stage;
import java.time.LocalDateTime;

public record MemberTicketResponse(Long id,
                                   Integer number,
                                   LocalDateTime entryTime,
                                   EntryState state,
                                   LocalDateTime reservedAt,
                                   StageResponse stage,
                                   MemberTicketFestivalResponse festival) {

    // TODO: FestivalResponse
    public static MemberTicketResponse from(MemberTicket memberTicket) {
        Stage stage = memberTicket.getStage();
        return new MemberTicketResponse(
            memberTicket.getId(),
            memberTicket.getNumber(),
            memberTicket.getEntryTime(),
            memberTicket.getEntryState(),
            LocalDateTime.now(), // TODO
            StageResponse.from(stage),
            MemberTicketFestivalResponse.from(stage.getFestival()));
    }
}
