package com.festago.festago.data.repository

import android.content.Context
import com.festago.festago.repository.SocialAuthRepository
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SocialAuthKakaoRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SocialAuthRepository {

    override val socialType: String = SOCIAL_TYPE_KAKAO

    override suspend fun getSocialToken(): Result<String> = runCatching {
        if (AuthApiClient.instance.hasToken()) {
            val error = accessTokenInfo()

            if (error is KakaoSdkError && error.isInvalidTokenError()) {
                loginWithKakao(context)
            } else if (error != null) {
                throw error
            }
        } else {
            loginWithKakao(context)
        }

        TokenManagerProvider.instance.manager.getToken()?.accessToken
            ?: throw Exception("Unknown error")
    }

    private suspend fun accessTokenInfo(): Throwable? {
        return suspendCoroutine<Throwable?> { continuation ->
            UserApiClient.instance.accessTokenInfo { _, throwable ->
                continuation.resume(throwable)
            }
        }
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

    private suspend fun loginWithKakao(context: Context): OAuthToken {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            try {
                loginWithKakaoTalk(context)
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error
                loginWithKakaoAccount(context)
            }
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): OAuthToken {
        return suspendCoroutine<OAuthToken> { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    continuation.resume(token)
                } else {
                    continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context): OAuthToken {
        return suspendCoroutine<OAuthToken> { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    continuation.resume(token)
                } else {
                    continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
                }
            }
        }
    }

    companion object {
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
    }
}
