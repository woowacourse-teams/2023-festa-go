package com.festago.festago.data.repository

import android.content.Context
import com.festago.festago.presentation.util.loginWithKakao
import com.festago.festago.repository.SocialAuthRepository
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class SocialAuthKakaoRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SocialAuthRepository {

    override val socialType: String = SOCIAL_TYPE_KAKAO

    override suspend fun getSocialToken(): Result<String> = runCatching {
        val tokenManger = TokenManagerProvider.instance.manager
        val oAuthToken = tokenManger.getToken()

        when {
            oAuthToken == null || oAuthToken.refreshTokenExpiresAt < Date() ->
                UserApiClient.loginWithKakao(context)

            else -> {
                UserApiClient.instance.accessTokenInfo { _, throwable ->
                    if (throwable != null) throw throwable
                }
            }
        }

        tokenManger.getToken()?.accessToken ?: throw Exception("Unknown error")
    }

    override suspend fun signOut(): Result<Unit> {
        UserApiClient.instance.logout {}
        return Result.success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit> {
        synchronized(this) {
            TokenManagerProvider.instance.manager.getToken()?.let {
                UserApiClient.instance.unlink { error ->
                    if (error != null) throw error
                }
            }
        }
        return Result.success(Unit)
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
    }
}
