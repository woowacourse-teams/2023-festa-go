package com.festago.festago.data.repository

import android.content.Context
import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.presentation.util.loginWithKakao
import com.festago.festago.repository.SocialAuthRepository
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class SocialAuthKakaoRepository @Inject constructor(
    private val userRetrofitService: UserRetrofitService,
    @ApplicationContext private val context: Context,
) : SocialAuthRepository {
    override val socialType: String = SOCIAL_TYPE_KAKAO

    override suspend fun getSocialToken(): Result<String> = runCatching {
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
        accessToken
    }

    override suspend fun signOut(): Result<Unit> {
        UserApiClient.instance.logout {}
        return Result.success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit> {
        UserApiClient.instance.unlink { error ->
            if (error != null) throw error
        }
        return Result.success(Unit)
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
    }
}
