package com.festago.domain;

public interface EntryCodeProvider {

    String provide(MemberTicket memberTicket, long period);
}
