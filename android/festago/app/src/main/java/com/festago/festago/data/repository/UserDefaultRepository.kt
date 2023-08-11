package com.festago.festago.data.repository

import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.domain.model.UserProfile
import com.festago.festago.domain.repository.UserRepository

class UserDefaultRepository(
    private val userProfileService: UserRetrofitService,
) : UserRepository {
    override suspend fun loadUserProfile(): Result<UserProfile> {
        userProfileService.getUserProfile().runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }
}
