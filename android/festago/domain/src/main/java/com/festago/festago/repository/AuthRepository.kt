package com.festago.festago.repository

interface AuthRepository {
    val isSigned: Boolean
    suspend fun signIn(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}
