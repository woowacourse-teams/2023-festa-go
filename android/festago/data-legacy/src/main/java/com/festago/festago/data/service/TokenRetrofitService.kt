package com.festago.festago.data.service

import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.dto.OauthTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenRetrofitService {

    @POST("/auth/oauth2")
    suspend fun getOauthToken(
        @Body oauthRequest: OauthRequest,
    ): Response<OauthTokenResponse>

    @DELETE("/auth")
    suspend fun deleteUserAccount(
        @Header("authorization") token: String,
    ): Response<Unit>
}
