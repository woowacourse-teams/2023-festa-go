package com.festago.domain.repository

import com.festago.domain.model.UserProfile

interface UserRepository {
    suspend fun loadUserProfile(): Result<UserProfile>
}
