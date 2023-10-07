package com.festago.festago.data.repository

import android.content.Context
import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.presentation.util.loginWithKakao
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TokenRepository
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class AuthKakaoRepository @Inject constructor(
    private val userRetrofitService: UserRetrofitService,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context,
) : AuthRepository {

    override val isSigned: Boolean
        get() = tokenRepository.token != null

    override suspend fun signIn(): Result<Unit> = runCatching {
        val oAuthToken = TokenManagerProvider.instance.manager.getToken()

        when {
            oAuthToken == null || oAuthToken.refreshTokenExpiresAt < Date() ->
                UserApiClient.loginWithKakao(context)

            oAuthToken.accessTokenExpiresAt < Date() -> {
                AuthApiClient.instance.refreshToken { _, throwable ->
                    if (throwable != null) {
                        throw throwable
                    }
                }
            }
        }

        val accessToken = TokenManagerProvider.instance.manager.getToken()?.accessToken
            ?: throw Exception("Unknown error")
        tokenRepository.initToken(SOCIAL_TYPE_KAKAO, accessToken)
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

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
    }
}
