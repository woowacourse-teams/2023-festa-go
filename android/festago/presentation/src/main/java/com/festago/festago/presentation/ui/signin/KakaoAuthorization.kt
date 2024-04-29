package com.festago.festago.presentation.ui.signin

import android.content.Context
import com.festago.festago.domain.model.nonce.NonceGenerator
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthorization(private val nonceGenerator: NonceGenerator) {

    suspend fun getIdToken(context: Context): Result<String> {
        return runCatching {
            val nonce = nonceGenerator.generate()
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                try {
                    loginWithKakaoTalk(context, nonce)
                } catch (error: Throwable) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error
                    loginWithKakaoAccount(context, nonce)
                }
            } else {
                loginWithKakaoAccount(context, nonce)
            }
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context, nonce: String?): String {
        return suspendCoroutine<String> { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context, nonce = nonce) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    if (token.idToken != null) {
                        continuation.resume(token.idToken!!)
                    } else {
                        continuation.resumeWithException(RuntimeException("Failure get kakao id token"))
                    }
                } else {
                    continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context, nonce: String?): String {
        return suspendCoroutine<String> { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context, nonce = nonce) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    if (token.idToken != null) {
                        continuation.resume(token.idToken!!)
                    } else {
                        continuation.resumeWithException(RuntimeException("Failure get kakao id token"))
                    }
                } else {
                    continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
                }
            }
        }
    }

    suspend fun signOut(): Result<Unit> {
        UserApiClient.instance.logout {}
        return Result.success(Unit)
    }

    suspend fun deleteAccount(): Result<Unit> {
        return suspendCoroutine<Result<Unit>> { continuation ->
            TokenManagerProvider.instance.manager.getToken()?.let {
                UserApiClient.instance.unlink { error ->
                    if (error == null) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resumeWithException(error)
                    }
                }
            } ?: continuation.resume(Result.success(Unit))
        }
    }
}
