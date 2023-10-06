package com.festago.entry.domain;

import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.domain.MemberTicket;

public class EntryCodePayload {

    private final Long memberTicketId;
    private final EntryState entryState;

    public EntryCodePayload(Long memberTicketId, EntryState entryState) {
        validate(memberTicketId, entryState);
        this.memberTicketId = memberTicketId;
        this.entryState = entryState;
    }

    private void validate(Long memberTicketId, EntryState entryState) {
        if (memberTicketId == null) {
            throw new IllegalArgumentException("memberTicketId는 null이 될 수 없습니다.");
        }
        if (entryState == null) {
            throw new IllegalArgumentException("entryState는 null이 될 수 없습니다.");
        }
    }

    public static EntryCodePayload from(MemberTicket memberTicket) {
        return new EntryCodePayload(memberTicket.getId(), memberTicket.getEntryState());
    }

    public Long getMemberTicketId() {
        return memberTicketId;
    }

    public EntryState getEntryState() {
        return entryState;
    }
}
