package com.festago.entryalert.dto;

import java.time.LocalDateTime;

public record EntryAlertResponse(
    Long id,
    LocalDateTime alertTime
) {

}
