package com.festago.festago.data.retrofit

import com.festago.festago.repository.TokenRepository
import com.kakao.sdk.auth.TokenManagerProvider
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val tokenRepository: TokenRepository,
) {

    val token: String
        get() = tokenRepository.token ?: NULL_TOKEN

    fun refreshToken() {
        tokenRepository.refreshToken(
            socialType = "KAKAO",
            socialToken = TokenManagerProvider.instance.manager.getToken()?.accessToken ?: NULL_TOKEN,
        )
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
