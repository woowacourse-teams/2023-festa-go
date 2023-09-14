package com.festago.zentry.application;

import com.festago.zentry.domain.EntryCodePayload;

public interface EntryCodeExtractor {

    EntryCodePayload extract(String code);
}
