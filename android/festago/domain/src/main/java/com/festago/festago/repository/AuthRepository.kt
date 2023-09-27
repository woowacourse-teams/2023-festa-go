package com.festago.festago.repository

interface AuthRepository {
    val isSigned: Boolean
    val token: String?
    suspend fun signInLegacy(socialType: String, token: String): Result<Unit?>
    suspend fun signIn(socialType: String, token: String, fcmToken: String): Result<Unit?>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}
