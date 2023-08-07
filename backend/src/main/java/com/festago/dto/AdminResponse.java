package com.festago.dto;

import java.util.List;

public record AdminResponse(
    List<AdminTicketResponse> adminTickets,
    List<AdminStageResponse> adminStageResponse,
    List<AdminFestivalResponse> adminFestivalResponse) {

}
