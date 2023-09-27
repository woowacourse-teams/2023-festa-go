package com.festago.festago.data.service

import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.dto.OauthRequestLegacy
import com.festago.festago.data.dto.OauthTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRetrofitService {

    @POST("/auth/oauth2")
    suspend fun getOauthToken(
        @Body oauthRequestLegacy: OauthRequestLegacy,
    ): Response<OauthTokenResponse>

    @POST("/auth/oauth2")
    suspend fun getOauthToken(
        @Body oauthRequestLegacy: OauthRequest,
    ): Response<OauthTokenResponse>
}
