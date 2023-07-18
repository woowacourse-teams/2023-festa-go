package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.MemberTicket;
import java.time.LocalDateTime;

public record MemberTicketResponse(Long id, Integer number, LocalDateTime entryTime, EntryState state,
                                   StageResponse stage) {

    public static MemberTicketResponse from(MemberTicket memberTicket) {
        return new MemberTicketResponse(
                memberTicket.getId(),
                memberTicket.getNumber(),
                memberTicket.getTicket().getEntryTime(),
                memberTicket.getEntryState(),
                StageResponse.from(memberTicket.getTicket().getStage()));
    }
}
