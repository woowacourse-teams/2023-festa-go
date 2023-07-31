package com.festago.dto;

import com.festago.domain.MemberTicket;
import java.time.LocalDateTime;

public record TicketingResponse(Long id,
                                Integer number,
                                LocalDateTime entryTime) {

    public static TicketingResponse from(MemberTicket memberTicket) {
        return new TicketingResponse(
            memberTicket.getId(),
            memberTicket.getNumber(),
            memberTicket.getEntryTime());
    }
}
