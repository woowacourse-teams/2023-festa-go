package com.festago.festago.data.retrofit

import com.festago.festago.domain.repository.TokenRepository
import com.kakao.sdk.auth.TokenManagerProvider

class TokenManager(private val tokenRepository: TokenRepository) {

    val token: String
        get() = tokenRepository.token ?: NULL_TOKEN

    fun refreshToken() {
        tokenRepository.refreshToken(
            token = TokenManagerProvider.instance.manager.getToken()?.accessToken ?: NULL_TOKEN,
        )
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
