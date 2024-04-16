package com.festago.festago.data.repository

import com.festago.festago.domain.repository.AuthRepository
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    override var shouldSign: Boolean = true

    override val isSigned: Boolean = false

    override val token: String = ""

    override suspend fun signIn(code: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun signOut(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun rejectSignIn() {
        // handle reject sign in
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return Result.success(Unit)
    }
}
