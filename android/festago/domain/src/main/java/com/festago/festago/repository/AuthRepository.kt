package com.festago.festago.repository

interface AuthRepository {
    var token: String?
    suspend fun refreshToken(): Result<Unit>
    suspend fun initToken(socialType: String, socialToken: String): Result<Unit>
}
