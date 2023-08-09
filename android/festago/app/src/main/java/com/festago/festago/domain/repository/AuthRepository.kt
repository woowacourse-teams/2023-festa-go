package com.festago.festago.domain.repository

interface AuthRepository {
    val isSigned: Boolean
    val token: String?
    suspend fun signIn(socialType: String, token: String): Result<Unit?>
}
