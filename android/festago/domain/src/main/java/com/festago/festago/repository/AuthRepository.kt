package com.festago.festago.repository

interface AuthRepository {
    var token: String?
    val isSigned: Boolean
    suspend fun signIn(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun refreshSignIn(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}
