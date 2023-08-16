package com.festago.festago.domain.repository

interface TokenRepository {
    var token: String?
    fun refreshToken(token: String): Result<Unit>
    suspend fun signIn(socialType: String, token: String): Result<Unit>
}
