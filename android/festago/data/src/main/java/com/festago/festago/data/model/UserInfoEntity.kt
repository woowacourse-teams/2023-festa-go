package com.festago.festago.data.model

import com.festago.festago.domain.model.user.UserInfo

data class UserInfoEntity(
    val nickname: String,
    val profileImageUrl: String,
) {
    fun toDomain() = UserInfo(
        nickname = nickname,
        profileImageUrl = profileImageUrl,
    )
}
