package com.festago.festago.repository

interface TokenRepository {
    var token: String?
    fun refreshToken(socialType: String, socialToken: String): Result<Unit>
    suspend fun initToken(socialType: String, socialToken: String): Result<Unit>
}
