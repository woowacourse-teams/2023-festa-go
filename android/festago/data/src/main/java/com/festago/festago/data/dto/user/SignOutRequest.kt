package com.festago.festago.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SignOutRequest(
    val refreshToken: String,
)
