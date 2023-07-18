package com.festago.festagostaff.data.service

import com.festago.festagostaff.data.dto.TicketValidationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TicketRetrofitService {
    @POST("/staff/tickets/validation")
    suspend fun validateTicket(@Body code: String): Response<TicketValidationDto>
}
