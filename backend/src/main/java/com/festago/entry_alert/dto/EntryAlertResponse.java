package com.festago.entry_alert.dto;

import java.time.LocalDateTime;

public record EntryAlertResponse(
    Long id,
    LocalDateTime alertTime
) {

}
