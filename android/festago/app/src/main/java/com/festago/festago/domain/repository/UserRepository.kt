package com.festago.festago.domain.repository

import com.festago.festago.domain.model.UserProfile

interface UserRepository {
    suspend fun loadUserProfile(): Result<UserProfile>
}
