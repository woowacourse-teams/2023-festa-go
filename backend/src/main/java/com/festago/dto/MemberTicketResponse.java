package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.Festival;
import com.festago.domain.MemberTicket;
import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import java.time.LocalDateTime;

public record MemberTicketResponse(Long id, Integer number, LocalDateTime entryTime, EntryState state,
                                   LocalDateTime reservedAt, StageResponse stage, FestivalResponse festival) {

    // TODO: FestivalResponse
    // 다른 API의 FestivalResponse와 해당 API의 FestivalResponse 응답값 구성이 다릅니다. (얘가 원래는 좀 더 심플함)
    // 따라서 이를 통일하거나, 이름을 요청별로 다르게 가져가거나 할 것 같아요
    // 얘기를 나눠봐야 할 것 같습니다! 리뷰 부탁드려요~
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
            FestivalResponse.from(festival));
    }
}
