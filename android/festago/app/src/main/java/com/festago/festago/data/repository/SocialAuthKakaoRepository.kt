package com.festago.festago.data.repository

import android.content.Context
import com.festago.festago.presentation.util.loginWithKakao
import com.festago.festago.repository.SocialAuthRepository
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Thread.sleep
import javax.inject.Inject

class SocialAuthKakaoRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SocialAuthRepository {

    override val socialType: String = SOCIAL_TYPE_KAKAO

    override suspend fun getSocialToken(): Result<String> = runCatching {
        if (AuthApiClient.instance.hasToken()) {
            val error = synchronized(this) { accessTokenInfo() }

            if (error is KakaoSdkError && error.isInvalidTokenError()) {
                UserApiClient.loginWithKakao(context)
            } else if (error != null) {
                throw error
            }
        } else {
            UserApiClient.loginWithKakao(context)
        }

        TokenManagerProvider.instance.manager.getToken()?.accessToken
            ?: throw Exception("Unknown error")
    }

    private fun accessTokenInfo(): Throwable? {
        var lock = true
        var error: Throwable? = null
        UserApiClient.instance.accessTokenInfo { _, throwable ->
            error = throwable
            lock = false
        }

        while (lock) sleep(20)
        return error
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
