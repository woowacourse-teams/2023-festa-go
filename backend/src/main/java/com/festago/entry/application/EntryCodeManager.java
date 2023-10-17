package com.festago.entry.application;

import com.festago.entry.domain.EntryCode;
import com.festago.entry.domain.EntryCodePayload;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryCodeManager {

    private static final int MILLISECOND_FACTOR = 1_000;
    private static final int DEFAULT_PERIOD = 30;
    private static final int DEFAULT_OFFSET = 10;

    private final EntryCodeProvider entryCodeProvider;
    private final EntryCodeExtractor entryCodeExtractor;

    public EntryCode provide(EntryCodePayload entryCodePayload, long currentTimeMillis) {
        Date expiredAt = new Date(currentTimeMillis + (DEFAULT_PERIOD + DEFAULT_OFFSET) * MILLISECOND_FACTOR);
        String code = entryCodeProvider.provide(entryCodePayload, expiredAt);
        return new EntryCode(code, DEFAULT_PERIOD, DEFAULT_OFFSET);
    }

    public EntryCodePayload extract(String code) {
        return entryCodeExtractor.extract(code);
    }
}
