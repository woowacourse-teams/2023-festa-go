package com.festago.festago.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
class SignInRequest(
    val socialType: String,
    val code: String,
)
