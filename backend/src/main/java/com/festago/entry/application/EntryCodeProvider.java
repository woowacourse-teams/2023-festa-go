package com.festago.entry.application;

import com.festago.entry.domain.EntryCodePayload;
import java.util.Date;

public interface EntryCodeProvider {

    String provide(EntryCodePayload entryCodePayload, Date expiredAt);
}
