package com.festago.zentry.application;

import com.festago.zentry.domain.EntryCodePayload;
import java.util.Date;

public interface EntryCodeProvider {

    String provide(EntryCodePayload entryCodePayload, Date expiredAt);
}
