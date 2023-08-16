package com.festago.festago.data.retrofit

import com.festago.festago.domain.repository.TokenRepository
import com.kakao.sdk.auth.TokenManagerProvider
import kotlinx.coroutines.runBlocking

class TokenManager(private val tokenRepository: TokenRepository) {
    fun refreshToken() {
        runBlocking {
            tokenRepository.refreshToken(
                token = TokenManagerProvider.instance.manager.getToken()?.accessToken ?: NULL_TOKEN,
            )
        }
    }

    val token: String
        get() = tokenRepository.token ?: NULL_TOKEN

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
