package com.festago.festival.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DetailFestivalResponse(
    Long id,
    Long schoolId,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail,
    List<DetailStageResponse> stages) {

    public record DetailStageResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime ticketOpenTime,
        String lineUp,
        List<DetailTicketResponse> tickets
    ) {

        public record DetailTicketResponse(
            Long id,
            String ticketType,
            Integer totalAmount,
            Integer remainAmount
        ) {

        }
    }
}
