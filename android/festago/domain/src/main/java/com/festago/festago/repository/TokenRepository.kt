package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    fun refreshTokenLegacy(token: String): Result<Unit>
    fun refreshToken(token: String, fcmToken: String): Result<Unit>
    suspend fun signInLegacy(socialType: String, token: String): Result<Unit>
    suspend fun signIn(socialType: String, token: String, fcmToken: String): Result<Unit>
}
