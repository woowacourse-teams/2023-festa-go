package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;

public class EntryCodePayload {

    private final Long memberTicketId;
    private final EntryState entryState;

    public EntryCodePayload(Long memberTicketId, EntryState entryState) {
        validate(memberTicketId, entryState);
        this.memberTicketId = memberTicketId;
        this.entryState = entryState;
    }

    private void validate(Long memberTicketId, EntryState entryState) {
        if (memberTicketId == null || entryState == null) {
            throw new InternalServerException(ErrorCode.INVALID_ENTRY_CODE_PAYLOAD);
        }
    }

    public Long getMemberTicketId() {
        return memberTicketId;
    }

    public EntryState getEntryState() {
        return entryState;
    }
}
