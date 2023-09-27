package com.festago.festago.data.repository

import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TokenRepository
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class AuthDefaultRepository @Inject constructor(
    private val userRetrofitService: UserRetrofitService,
    private val tokenRepository: TokenRepository,
) : AuthRepository {

    override val isSigned: Boolean
        get() = tokenRepository.token != null

    override val token: String?
        get() = tokenRepository.token

    override suspend fun signIn(socialType: String, token: String, fcmToken: String): Result<Unit> {
        return tokenRepository.signIn(socialType, token, fcmToken)
    }

    override suspend fun signOut(): Result<Unit> {
        UserApiClient.instance.logout {
            tokenRepository.token = null
        }
        return Result.success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit> =
        runCatchingResponse { userRetrofitService.deleteUserAccount() }
            .onSuccessOrCatch {
                UserApiClient.instance.unlink { error ->
                    if (error == null) {
                        tokenRepository.token = null
                    }
                }
            }
}
