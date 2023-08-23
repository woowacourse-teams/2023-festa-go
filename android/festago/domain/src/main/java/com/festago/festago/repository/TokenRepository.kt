package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    fun refreshToken(token: String): Result<Unit>
    suspend fun signIn(socialType: String, token: String): Result<Unit>
}
