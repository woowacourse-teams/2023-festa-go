package com.festago.entry.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.entry.application.EntryCodeExtractor;
import com.festago.entry.domain.EntryCodePayload;
import lombok.SneakyThrows;

/**
 * Fake이므로 프로덕션에서 사용 금지!
 */
public class JsonEntryCodeExtractor implements EntryCodeExtractor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public EntryCodePayload extract(String code) {
        JsonEntryCodePayload payload = objectMapper.readValue(code, JsonEntryCodePayload.class);
        return new EntryCodePayload(payload.memberTicketId(), payload.entryState());
    }
}
