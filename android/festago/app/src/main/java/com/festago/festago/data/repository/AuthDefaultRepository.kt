package com.festago.festago.data.repository

import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.domain.repository.AuthRepository

class AuthDefaultRepository(
    private val authRetrofitService: AuthRetrofitService,
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override val isSigned: Boolean
        get() = authDataSource.token != null

    override val token: String?
        get() = authDataSource.token

    override suspend fun signIn(socialType: String, token: String): Result<Unit> {
        try {
            val response = authRetrofitService.getOauthToken(OauthRequest(socialType, token))
            if (response.isSuccessful && response.body() != null) {
                authDataSource.token = response.body()!!.accessToken
                return Result.success(Unit)
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
