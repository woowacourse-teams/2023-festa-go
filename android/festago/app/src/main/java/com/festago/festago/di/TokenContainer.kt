package com.festago.festago.di

import com.festago.festago.data.retrofit.TokenManager
import com.festago.festago.domain.repository.TokenRepository

class TokenContainer(tokenRepository: TokenRepository) {
    val tokenManager = TokenManager(tokenRepository)
}
