package com.festago.dto;

import com.festago.domain.EntryState;
import com.festago.domain.MemberTicket;

public record TicketValidationResponse(EntryState updatedState) {

    public static TicketValidationResponse from(MemberTicket memberTicket) {
        return new TicketValidationResponse(memberTicket.getEntryState());
    }
}
