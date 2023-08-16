package com.festago.festago.data.repository

import com.festago.domain.repository.AuthRepository
import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.dto.OauthRequest
import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.kakao.sdk.user.UserApiClient

class AuthDefaultRepository(
    private val authRetrofitService: AuthRetrofitService,
    private val authDataSource: AuthDataSource,
    private val userRetrofitService: UserRetrofitService,
) : AuthRepository {

    override val isSigned: Boolean
        get() = authDataSource.token != null

    override val token: String?
        get() = authDataSource.token

    override suspend fun signIn(socialType: String, token: String): Result<Unit> {
        authRetrofitService.getOauthToken(OauthRequest(socialType, token))
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let {
                authDataSource.token = it.accessToken
                return Result.success(Unit)
            }
    }

    override suspend fun signOut(): Result<Unit> {
        UserApiClient.instance.logout {
            authDataSource.token = null
        }
        return Result.success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit> {
        userRetrofitService.deleteUserAccount().runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let {
                UserApiClient.instance.unlink { error ->
                    if (error == null) {
                        authDataSource.token = null
                    }
                }
                return Result.success(Unit)
            }
    }
}
