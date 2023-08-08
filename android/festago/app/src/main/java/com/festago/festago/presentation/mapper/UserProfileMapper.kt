package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.UserProfile
import com.festago.festago.presentation.model.UserProfileUiModel

fun UserProfile.toPresentation(): UserProfileUiModel = UserProfileUiModel(nickName, profileImage)
