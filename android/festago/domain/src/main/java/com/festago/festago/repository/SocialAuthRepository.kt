package com.festago.festago.repository

interface SocialAuthRepository {
    val socialType: String
    suspend fun getSocialToken(): Result<String>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}
