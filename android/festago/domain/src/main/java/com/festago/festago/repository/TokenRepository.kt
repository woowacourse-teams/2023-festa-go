package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    fun refreshToken(socialType: String, token: String, fcmToken: String): Result<Unit>
    suspend fun signIn(socialType: String, token: String, fcmToken: String): Result<Unit>
}
