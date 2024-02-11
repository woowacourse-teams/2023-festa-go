package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.time.LocalDateTime;

public record StageV1Response(
    Long id,
    LocalDateTime startDate,
    @JsonRawValue
    String artists
) {

}
