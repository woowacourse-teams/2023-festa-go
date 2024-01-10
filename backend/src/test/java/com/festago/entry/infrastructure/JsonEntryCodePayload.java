package com.festago.entry.infrastructure;

import com.festago.ticketing.domain.EntryState;
import java.util.Date;

/**
 * Fake이므로 프로덕션에서 사용 금지!
 *
 * @param entryState
 * @param memberTicketId
 * @param expiredAt
 */
public record JsonEntryCodePayload(
    EntryState entryState,
    Long memberTicketId,
    Date expiredAt
) {

}
