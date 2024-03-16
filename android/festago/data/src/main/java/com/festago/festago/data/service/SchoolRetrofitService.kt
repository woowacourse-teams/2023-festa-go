package com.festago.festago.data.service

import com.festago.festago.data.dto.schooldetail.SchoolFestivalsResponse
import com.festago.festago.data.dto.schooldetail.SchoolInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface SchoolRetrofitService {
    @GET("api/v1/schools/{schoolId}")
    suspend fun getSchool(
        @Path("schoolId") schoolId: Long,
    ): Response<SchoolInfoResponse>

    @GET("api/v1/schools/{schoolId}/festivals")
    suspend fun getSchoolFestivals(
        @Path("schoolId") schoolId: Long,
        @Query("size") size: Int?,
        @Query("isPast") isPast: Boolean?,
        @Query("lastFestivalId") lastFestivalId: Int?,
        @Query("lastStartDate") lastStartDate: LocalDate?,
    ): Response<SchoolFestivalsResponse>
}
