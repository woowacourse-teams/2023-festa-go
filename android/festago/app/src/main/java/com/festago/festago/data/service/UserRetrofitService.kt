package com.festago.festago.data.service

import com.festago.festago.data.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserRetrofitService {
    @GET("/members/profile")
    suspend fun getUserProfile(): Response<UserProfileResponse>
}
