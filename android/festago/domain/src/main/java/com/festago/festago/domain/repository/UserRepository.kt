package com.festago.festago.domain.repository

import com.festago.festago.domain.model.token.Token

interface UserRepository {
    suspend fun isSignRejected(): Boolean
    suspend fun isSigned(): Boolean
    suspend fun getAccessToken(): Result<Token>
    suspend fun getRefreshToken(): Result<Token>
    suspend fun signIn(idToken: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun rejectSignIn()
}
