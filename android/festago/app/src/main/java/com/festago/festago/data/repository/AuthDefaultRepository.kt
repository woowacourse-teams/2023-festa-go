package com.festago.festago.data.repository

import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.domain.model.Token
import com.festago.festago.domain.repository.AuthRepository

class AuthDefaultRepository(
    private val authRetrofitService: AuthRetrofitService,
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override val isSigned: Boolean
        get() = authDataSource.token != null

    override suspend fun signIn(socialType: String, token: String): Result<Token> {
        try {
            val response = authRetrofitService.getOauthToken(OauthRequest(socialType, token))
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toToken())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun findToken(): String? = authDataSource.token

    override fun storeToken(token: String) {
        authDataSource.token = token
    }
}
