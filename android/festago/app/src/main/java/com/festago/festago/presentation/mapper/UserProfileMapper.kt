package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.UserProfile
import com.festago.festago.presentation.model.UserProfileUiModel

fun UserProfile.toPresentation(): UserProfileUiModel = UserProfileUiModel(
    memberId = memberId,
    nickName = nickName,
    profileImage = profileImage,
)
