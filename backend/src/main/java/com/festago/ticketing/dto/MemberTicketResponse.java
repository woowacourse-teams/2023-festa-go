package com.festago.ticketing.dto;

import com.festago.stage.domain.Stage;
import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.domain.MemberTicket;
import java.time.LocalDateTime;

@Deprecated(forRemoval = true)
public record MemberTicketResponse(
    Long id,
    Integer number,
    LocalDateTime entryTime,
    EntryState state,
    LocalDateTime reservedAt,
//    StageResponse stage, // DTO 객체 새롭게 만들 것
    MemberTicketFestivalResponse festival
) {

    public static MemberTicketResponse from(MemberTicket memberTicket) {
        Stage stage = memberTicket.getStage();
        return new MemberTicketResponse(
            memberTicket.getId(),
            memberTicket.getNumber(),
            memberTicket.getEntryTime(),
            memberTicket.getEntryState(),
            memberTicket.getCreatedAt(),
            MemberTicketFestivalResponse.from(stage.getFestival()));
    }
}
