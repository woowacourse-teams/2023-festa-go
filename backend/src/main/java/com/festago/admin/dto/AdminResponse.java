package com.festago.admin.dto;

import java.util.List;

public record AdminResponse(

    List<AdminSchoolResponse> adminSchools,

    List<AdminTicketResponse> adminTickets,
    List<AdminStageResponse> adminStageResponse,
    List<AdminFestivalResponse> adminFestivalResponse) {

}
