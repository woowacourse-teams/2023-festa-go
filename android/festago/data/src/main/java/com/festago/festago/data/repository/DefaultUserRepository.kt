package com.festago.festago.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.token.Token
import com.festago.festago.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val authRetrofitService: AuthRetrofitService,
    private val tokenDataSource: TokenDataSource,
    @ApplicationContext context: Context,
) : UserRepository {

    private val sharedPreference: SharedPreferences by lazy {
        context.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE)
    }

    override suspend fun isSigned() = getRefreshToken().isSuccess

    override suspend fun isSignRejected() = sharedPreference.getBoolean(IS_SIGN_REJECTED, false)

    override suspend fun getAccessToken(): Result<Token> {
        val token = tokenDataSource.accessToken?.toDomain()
            ?: return Result.failure(Throwable("Access token is null"))

        if (!token.isExpired()) {
            return Result.success(token)
        }

        return try {
            val refreshToken = getRefreshToken().getOrThrow()
            refresh(refreshToken).getOrThrow()
            Result.success(tokenDataSource.accessToken?.toDomain()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRefreshToken(): Result<Token> {
        val refreshToken = tokenDataSource.refreshToken?.toDomain()
            ?: return Result.failure(Throwable("Refresh token is null"))

        return if (refreshToken.isExpired()) {
            Result.failure(Throwable("Refresh token is expired"))
        } else {
            Result.success(refreshToken)
        }
    }

    override suspend fun signIn(code: String): Result<Unit> {
        return runCatchingResponse {
            userRetrofitService.signIn(code)
        }.onSuccessOrCatch { signInResponse ->
            tokenDataSource.accessToken = signInResponse.accessToken.toEntity()
            tokenDataSource.accessToken = signInResponse.refreshToken.toEntity()
        }
    }

    override suspend fun rejectSignIn() {
        if (isSigned() || isSignRejected()) return
        sharedPreference.edit().putBoolean(IS_SIGN_REJECTED, true).apply()
    }

    override suspend fun signOut(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(): Result<Unit> {
        TODO("Not yet implemented")
    }

    private suspend fun refresh(refreshToken: Token): Result<Unit> {
        return runCatchingResponse {
            authRetrofitService.refresh(RefreshRequest(refreshToken.token))
        }.onSuccessOrCatch { refreshTokenResponse ->
            tokenDataSource.accessToken = refreshTokenResponse.accessToken.toEntity()
        }
    }

    companion object {
        private const val AUTH_PREF = "auth_pref"
        private const val IS_SIGN_REJECTED = "is_sign_rejected"
    }
}
