package com.festago.festago.presentation.ui.signin

import android.content.Context
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthorization {

    suspend fun getAuthCode(context: Context): Result<String> {
        return runCatching {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                authorizeWithKakaoTalk(context)
            } else {
                authorizeWithKakaoAccount(context)
            }
        }
    }
}

private suspend fun authorizeWithKakaoTalk(context: Context): String {
    return suspendCoroutine<String> { continuation ->
        AuthCodeClient.instance.authorizeWithKakaoTalk(context) { code: String?, error: Throwable? ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (code != null) {
                continuation.resume(code)
            } else {
                continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
            }
        }
    }
}

private suspend fun authorizeWithKakaoAccount(context: Context): String {
    return suspendCoroutine<String> { continuation ->
        AuthCodeClient.instance.authorizeWithKakaoAccount(context) { code: String?, error: Throwable? ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (code != null) {
                continuation.resume(code)
            } else {
                continuation.resumeWithException(RuntimeException("Failure get kakao access token"))
            }
        }
    }
}
