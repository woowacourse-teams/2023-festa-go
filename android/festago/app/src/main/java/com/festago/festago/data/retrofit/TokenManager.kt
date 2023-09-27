package com.festago.festago.data.retrofit

import com.festago.festago.repository.TokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.TokenManagerProvider
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val firebaseMessaging: FirebaseMessaging,
) {

    val token: String
        get() = tokenRepository.token ?: NULL_TOKEN

    fun refreshToken() {
        runBlocking {
            tokenRepository.refreshToken(
                socialType = "KAKAO",
                token = TokenManagerProvider.instance.manager.getToken()?.accessToken ?: NULL_TOKEN,
                fcmToken = firebaseMessaging.token.await() ?: NULL_TOKEN,
            )
        }
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
