package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    fun refreshToken(socialType: String, token: String): Result<Unit>
    suspend fun initToken(socialType: String, token: String): Result<Unit>
}
