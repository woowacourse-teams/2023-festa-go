package com.festago.zentry.dto;

import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.domain.MemberTicket;

public record TicketValidationResponse(
    EntryState updatedState) {

    public static TicketValidationResponse from(MemberTicket memberTicket) {
        return new TicketValidationResponse(memberTicket.getEntryState());
    }
}
