package com.festago.festago.data.repository

import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.TokenRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDefaultRepository @Inject constructor(
    private val tokenLocalDataSource: TokenDataSource,
    private val tokenRetrofitService: TokenRetrofitService,
) : TokenRepository {
    override var token: String?
        get() = tokenLocalDataSource.token
        set(value) {
            tokenLocalDataSource.token = value
        }

    override suspend fun signIn(socialType: String, token: String): Result<Unit> {
        tokenRetrofitService.getOauthToken(OauthRequest(socialType, token))
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let {
                tokenLocalDataSource.token = it.accessToken
                return Result.success(Unit)
            }
    }

    override fun refreshToken(token: String): Result<Unit> = runBlocking {
        tokenRetrofitService.getOauthToken(OauthRequest("KAKAO", token))
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return@runBlocking Result.failure(error) }
            .let {
                tokenLocalDataSource.token = it.accessToken
                return@runBlocking Result.success(Unit)
            }
    }
}
