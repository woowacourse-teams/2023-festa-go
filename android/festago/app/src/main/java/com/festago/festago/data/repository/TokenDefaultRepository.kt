package com.festago.festago.data.repository

import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.TokenRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.repository.TokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TokenDefaultRepository @Inject constructor(
    private val tokenLocalDataSource: TokenDataSource,
    private val tokenRetrofitService: TokenRetrofitService,
    private val firebaseMessaging: FirebaseMessaging,
) : TokenRepository {
    override var token: String?
        get() = tokenLocalDataSource.token
        set(value) {
            tokenLocalDataSource.token = value
        }

    override suspend fun initToken(socialType: String, socialToken: String): Result<Unit> =
        runCatchingResponse {
            val fcmToken = firebaseMessaging.token.await()
            tokenRetrofitService.getOauthToken(OauthRequest(socialType, socialToken, fcmToken))
        }.onSuccessOrCatch { tokenLocalDataSource.token = it.accessToken }

    override suspend fun refreshToken(socialType: String, socialToken: String): Result<Unit> =
        runCatchingResponse {
            val fcmToken = firebaseMessaging.token.await()
            tokenRetrofitService.getOauthToken(OauthRequest(socialType, socialToken, fcmToken))
        }.onSuccessOrCatch { tokenLocalDataSource.token = it.accessToken }
}
