package com.festago.festago.data.service

import com.festago.festago.data.dto.SchoolsResponse
import retrofit2.Response
import retrofit2.http.GET

interface SchoolRetrofitService {

    @GET("/schools")
    suspend fun getSchools(): Response<SchoolsResponse>
}
