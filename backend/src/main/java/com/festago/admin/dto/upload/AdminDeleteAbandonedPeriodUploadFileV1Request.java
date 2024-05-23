package com.festago.admin.dto.upload;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AdminDeleteAbandonedPeriodUploadFileV1Request(
    @NotNull LocalDateTime startTime,
    @NotNull LocalDateTime endTime
) {

}
