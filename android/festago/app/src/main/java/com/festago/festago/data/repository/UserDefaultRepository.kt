package com.festago.festago.data.repository

import com.festago.festago.domain.model.UserProfile
import com.festago.festago.domain.repository.UserRepository
import kotlinx.coroutines.delay

class UserDefaultRepository : UserRepository {
    override suspend fun loadUserProfile(): Result<UserProfile> {
        delay(500)
        return Result.success(
            UserProfile(
                "홍길동",
                "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
            ),
        )
    }
}
