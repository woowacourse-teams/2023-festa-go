package com.festago.entry.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.common.exception.UnexpectedException;
import com.festago.entry.application.EntryCodeProvider;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.ticketing.domain.EntryState;
import java.util.Date;
import lombok.SneakyThrows;

/**
 * Fake이므로 프로덕션에서 사용 금지!
 */
public class JsonEntryCodeProvider implements EntryCodeProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String provide(EntryCodePayload entryCodePayload, Date expiredAt) {
        validate(expiredAt);
        EntryState entryState = entryCodePayload.getEntryState();
        Long memberTicketId = entryCodePayload.getMemberTicketId();
        JsonEntryCodePayload payload = new JsonEntryCodePayload(entryState, memberTicketId, expiredAt);
        return objectMapper.writeValueAsString(payload);
    }

    private void validate(Date expiredAt) {
        if (expiredAt.before(new Date())) {
            throw new UnexpectedException("입장코드 만료일자는 과거일 수 없습니다.");
        }
    }
}
