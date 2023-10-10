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
    private val socialAuthRepository: SocialAuthRepository,
    private val tokenRetrofitService: TokenRetrofitService,
    private val tokenDataSource: TokenDataSource,
    private val firebaseMessaging: FirebaseMessaging,
) : AuthRepository {
    override var token: String?
        get() = tokenDataSource.token
        set(value) {
            tokenDataSource.token = value
        }

    override val isSigned: Boolean
        get() = tokenDataSource.token != null

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
        }.onSuccessOrCatch { tokenDataSource.token = it.accessToken }

    override suspend fun signOut(): Result<Unit> {
        tokenDataSource.token = null
        return socialAuthRepository.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit> = runCatchingResponse {
        socialAuthRepository.deleteAccount()
        tokenRetrofitService.deleteUserAccount("Bearer ${tokenDataSource.token}")
    }.apply { tokenDataSource.token = null }
}
