package com.festago.festago.domain.repository

import com.festago.festago.domain.model.user.Token
import com.festago.festago.domain.model.user.UserInfo

interface UserRepository {
    suspend fun isSignRejected(): Boolean
    suspend fun isSigned(): Boolean
    suspend fun getAccessToken(): Result<Token>
    suspend fun getRefreshToken(): Result<Token>
    suspend fun signIn(idToken: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun rejectSignIn()
    suspend fun getUserInfo(): Result<UserInfo>
    suspend fun clearToken()
}
