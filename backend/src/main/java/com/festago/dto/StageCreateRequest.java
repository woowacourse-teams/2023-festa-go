package com.festago.dto;

import java.time.LocalDateTime;

public record StageCreateRequest(LocalDateTime startTime,
                                 String lineUp,
                                 LocalDateTime ticketOpenTime,
                                 Long festivalId) {

}
