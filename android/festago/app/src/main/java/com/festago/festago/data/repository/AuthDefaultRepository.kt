package com.festago.festago.data.repository

import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.TokenRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.SocialAuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDefaultRepository @Inject constructor(
    private val tokenLocalDataSource: TokenDataSource,
    private val tokenRetrofitService: TokenRetrofitService,
    private val firebaseMessaging: FirebaseMessaging,
    private val socialAuthRepository: SocialAuthRepository,
) : AuthRepository {
    override var token: String?
        get() = tokenLocalDataSource.token
        set(value) {
            tokenLocalDataSource.token = value
        }

    override val isSigned: Boolean
        get() = tokenLocalDataSource.token != null

    override suspend fun signIn(): Result<Unit> =
        runCatchingResponse {
            val fcmToken = firebaseMessaging.token.await()
            tokenRetrofitService.getOauthToken(
                OauthRequest(
                    socialAuthRepository.socialType,
                    socialAuthRepository.getSocialToken().getOrThrow(),
                    fcmToken,
                ),
            )
        }.onSuccessOrCatch { tokenLocalDataSource.token = it.accessToken }

    override suspend fun signOut(): Result<Unit> {
        val result = socialAuthRepository.signOut()
        if (result.isSuccess) {
            tokenLocalDataSource.token = null
        }
        return result
    }

    override suspend fun deleteAccount(): Result<Unit> {
        val result = socialAuthRepository.deleteAccount()
        if (result.isSuccess) {
            tokenLocalDataSource.token = null
        }
        return result
    }
}
