package com.festago.festago.data.service

import com.festago.festago.data.dto.user.RefreshRequest
import com.festago.festago.data.dto.user.RefreshResponse
import com.festago.festago.data.dto.user.SignInRequest
import com.festago.festago.data.dto.user.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitService {
    @POST("api/v1/auth/login/oauth2")
    suspend fun signIn(
        @Body signInRequest: SignInRequest,
    ): Response<SignInResponse>

    @POST("api/v1/auth/refresh")
    suspend fun refresh(
        @Body refreshRequest: RefreshRequest,
    ): Response<RefreshResponse>

    @POST("api/v1/auth/logout")
    suspend fun signOut(
        @Header("Authorization") token: String,
    ): Response<Unit>

    @DELETE("api/v1/auth")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
    ): Response<Unit>
}
