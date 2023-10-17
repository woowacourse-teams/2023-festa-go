package com.festago.entry.application;

import com.festago.entry.domain.EntryCodePayload;

public interface EntryCodeExtractor {

    EntryCodePayload extract(String code);
}
