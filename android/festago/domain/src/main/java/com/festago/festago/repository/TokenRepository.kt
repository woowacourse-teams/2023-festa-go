package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    suspend fun refreshToken(): Result<Unit>
    suspend fun initToken(socialType: String, socialToken: String): Result<Unit>
}
