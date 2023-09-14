package com.festago.application;

import com.festago.domain.EntryCode;
import com.festago.domain.EntryCodeExtractor;
import com.festago.domain.EntryCodePayload;
import com.festago.domain.EntryCodeProvider;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class EntryCodeManager {

    private static final int MILLISECOND_FACTOR = 1_000;
    private static final int DEFAULT_PERIOD = 30;
    private static final int DEFAULT_OFFSET = 10;

    private final EntryCodeProvider entryCodeProvider;
    private final EntryCodeExtractor entryCodeExtractor;

    public EntryCodeManager(EntryCodeProvider entryCodeProvider, EntryCodeExtractor entryCodeExtractor) {
        this.entryCodeProvider = entryCodeProvider;
        this.entryCodeExtractor = entryCodeExtractor;
    }

    public EntryCode provide(EntryCodePayload entryCodePayload, long currentTimeMillis) {
        Date expiredAt = new Date(currentTimeMillis + (DEFAULT_PERIOD + DEFAULT_OFFSET) * MILLISECOND_FACTOR);
        String code = entryCodeProvider.provide(entryCodePayload, expiredAt);
        return new EntryCode(code, DEFAULT_PERIOD, DEFAULT_OFFSET);
    }

    public EntryCodePayload extract(String code) {
        return entryCodeExtractor.extract(code);
    }
}
