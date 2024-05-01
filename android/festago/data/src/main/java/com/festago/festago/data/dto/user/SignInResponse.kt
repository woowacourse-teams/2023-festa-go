package com.festago.festago.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val nickname: String,
    val profileImageUrl: String,
    val accessToken: TokenResponse,
    val refreshToken: TokenResponse,
)
