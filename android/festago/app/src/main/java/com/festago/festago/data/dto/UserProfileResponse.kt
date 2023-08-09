package com.festago.festago.data.dto

import com.festago.festago.domain.model.UserProfile

data class UserProfileResponse(
    val memberId: Long,
    val nickname: String,
    val profileImage: String,
) {
    fun toDomain(): UserProfile = UserProfile(memberId, nickname, profileImage)
}
