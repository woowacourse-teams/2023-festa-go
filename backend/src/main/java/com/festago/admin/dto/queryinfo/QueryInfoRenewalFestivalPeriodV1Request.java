package com.festago.admin.dto.queryinfo;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record QueryInfoRenewalFestivalPeriodV1Request(
    @NotNull LocalDate to,
    @NotNull LocalDate end
) {

}
