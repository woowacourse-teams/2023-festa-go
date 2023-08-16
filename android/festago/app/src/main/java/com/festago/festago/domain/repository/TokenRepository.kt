package com.festago.festago.domain.repository

interface TokenRepository {
    val token: String?
    fun refreshToken(token: String): Result<Unit>
}
