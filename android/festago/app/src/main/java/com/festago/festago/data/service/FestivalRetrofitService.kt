package com.festago.festago.data.service

import com.festago.festago.data.dto.FestivalsResponse
import retrofit2.Response
import retrofit2.http.GET

interface FestivalRetrofitService {
    @GET("/festivals")
    suspend fun getFestivals(): Response<FestivalsResponse>
}
