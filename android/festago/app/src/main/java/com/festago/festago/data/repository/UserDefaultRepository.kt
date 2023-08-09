package com.festago.festago.data.repository

import com.festago.festago.data.service.UserRetrofitService
import com.festago.festago.domain.model.UserProfile
import com.festago.festago.domain.repository.UserRepository

class UserDefaultRepository(
    private val userProfileService: UserRetrofitService,
) : UserRepository {
    override suspend fun loadUserProfile(): Result<UserProfile> {
        try {
            val response = userProfileService.getUserProfile()
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
