package com.festago.festago.repository

import com.festago.festago.model.UserProfile

interface UserRepository {
    suspend fun loadUserProfile(): Result<UserProfile>
}
