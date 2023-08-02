package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.MemberTicket;
import com.festago.domain.Stage;
import java.time.LocalDateTime;

public record CurrentMemberTicketResponse(Long id,
                                          Integer number,
                                          LocalDateTime entryTime,
                                          EntryState state,
                                          CurrentStageResponse stage,
                                          CurrentFestivalResponse festival
) {

    public static CurrentMemberTicketResponse from(MemberTicket memberTicket) {
        Stage stage = memberTicket.getStage();
        return new CurrentMemberTicketResponse(
            memberTicket.getId(),
            memberTicket.getNumber(),
            memberTicket.getEntryTime(),
            memberTicket.getEntryState(),
            CurrentStageResponse.from(stage),
            CurrentFestivalResponse.from(stage.getFestival())
        );
    }
}
