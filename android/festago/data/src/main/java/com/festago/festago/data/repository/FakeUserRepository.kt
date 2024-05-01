package com.festago.festago.data.repository

import com.festago.festago.domain.model.user.Token
import com.festago.festago.domain.model.user.UserInfo
import com.festago.festago.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    override suspend fun isSignRejected(): Boolean {
        return false
    }

    override suspend fun isSigned(): Boolean {
        return true
    }

    override suspend fun getRefreshToken(): Result<Token> {
        return Result.success(Token("", LocalDateTime.now()))
    }

    override suspend fun getAccessToken(): Result<Token> {
        return Result.success(Token("", LocalDateTime.now()))
    }

    override suspend fun signIn(idToken: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun signOut(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun rejectSignIn() {
        // handle reject sign in
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getUserInfo(): Result<UserInfo> {
        return Result.success(UserInfo("", ""))
    }
}
