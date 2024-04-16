package com.festago.festago.domain.repository

interface AuthRepository {
    val shouldSign: Boolean
    val isSigned: Boolean
    val token: String?
    suspend fun signIn(code: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    fun rejectSignIn()
}
