package com.festago.festago.data.repository

import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking

class TokenDefaultRepository(
    private val authLocalDataSource: AuthDataSource,
    private val authRetrofitService: AuthRetrofitService,
) : TokenRepository {
    override val token: String?
        get() = authLocalDataSource.token

    override fun refreshToken(token: String): Result<Unit> {
        return runBlocking {
            authRetrofitService.getOauthToken(
                oauthRequest = OauthRequest(
                    socialType = "KAKAO",
                    accessToken = token,
                ),
            ).runCatchingWithErrorHandler()
                .getOrElse { error -> return@runBlocking Result.failure(error) }
                .let {
                    authLocalDataSource.token = it.accessToken
                    return@runBlocking Result.success(Unit)
                }
        }
    }
}
