package com.festago.domain;

public class EntryCodePayload {

    private final Long memberTicketId;
    private final EntryState entryState;

    public EntryCodePayload(Long memberTicketId, EntryState entryState) {
        validate(memberTicketId, entryState);
        this.memberTicketId = memberTicketId;
        this.entryState = entryState;
    }

    public static EntryCodePayload from(MemberTicket memberTicket) {
        return new EntryCodePayload(memberTicket.getId(), memberTicket.getEntryState());
    }

    private void validate(Long memberTicketId, EntryState entryState) {
        if (memberTicketId == null) {
            throw new IllegalArgumentException(); // TODO
        }
        if (entryState == null) {
            throw new IllegalArgumentException(); // TODO
        }
    }

    public Long getMemberTicketId() {
        return memberTicketId;
    }

    public EntryState getEntryState() {
        return entryState;
    }
}
