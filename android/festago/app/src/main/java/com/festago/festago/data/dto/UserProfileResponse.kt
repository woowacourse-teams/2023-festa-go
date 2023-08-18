package com.festago.festago.data.dto

import com.festago.festago.model.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val memberId: Long,
    val nickname: String,
    val profileImage: String?,
) {
    fun toDomain(): UserProfile = UserProfile(
        memberId = memberId,
        nickName = nickname,
        profileImage = profileImage ?: "",
    )
}
