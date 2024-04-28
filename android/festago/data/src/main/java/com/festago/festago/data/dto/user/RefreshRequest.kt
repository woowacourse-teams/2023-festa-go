package com.festago.festago.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
class RefreshRequest(
    val refreshToken: String,
)
