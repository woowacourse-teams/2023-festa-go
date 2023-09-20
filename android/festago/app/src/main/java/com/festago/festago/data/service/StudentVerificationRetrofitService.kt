package com.festago.festago.data.service

import com.festago.festago.data.dto.SendVerificationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StudentVerificationRetrofitService {
    @POST("/students/send-verification")
    suspend fun sendVerificationCode(
        @Body sendVerificationRequest: SendVerificationRequest,
    ): Response<Unit>
}
