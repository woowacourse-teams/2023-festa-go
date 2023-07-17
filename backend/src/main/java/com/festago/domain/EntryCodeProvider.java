package com.festago.domain;

import java.util.Date;

public interface EntryCodeProvider {

    String provide(MemberTicket memberTicket, Date expiredAt);
}
