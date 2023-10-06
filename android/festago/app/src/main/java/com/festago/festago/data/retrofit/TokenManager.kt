package com.festago.festago.data.retrofit

import com.festago.festago.repository.TokenRepository
import com.kakao.sdk.auth.TokenManagerProvider
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val tokenRepository: TokenRepository,
) {

    val token: String
        get() = tokenRepository.token ?: NULL_TOKEN

    fun refreshToken() {
        runBlocking {
            tokenRepository.refreshToken(
                socialType = "KAKAO",
                token = TokenManagerProvider.instance.manager.getToken()?.accessToken ?: NULL_TOKEN,
            )
        }
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
