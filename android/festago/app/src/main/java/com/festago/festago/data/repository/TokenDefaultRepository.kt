package com.festago.festago.data.repository

import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.TokenRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runRetrofitWithErrorHandler
import com.festago.festago.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenDefaultRepository @Inject constructor(
    private val tokenLocalDataSource: TokenDataSource,
    private val tokenRetrofitService: TokenRetrofitService,
) : TokenRepository {
    override var token: String?
        get() = tokenLocalDataSource.token
        set(value) {
            tokenLocalDataSource.token = value
        }

    override suspend fun signIn(socialType: String, token: String): Result<Unit> =
        runRetrofitWithErrorHandler {
            tokenRetrofitService.getOauthToken(OauthRequest(socialType, token))
        }.onSuccessOrCatch { tokenLocalDataSource.token = it.accessToken }

    override fun refreshToken(token: String): Result<Unit> = runBlocking {
        runRetrofitWithErrorHandler {
            tokenRetrofitService.getOauthToken(OauthRequest("KAKAO", token))
        }.onSuccessOrCatch { tokenLocalDataSource.token = it.accessToken }
    }
}
