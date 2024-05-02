package com.festago.festago.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    val accessToken: TokenResponse,
    val refreshToken: TokenResponse,
)
