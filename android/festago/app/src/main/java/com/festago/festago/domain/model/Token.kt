package com.festago.festago.domain.model

data class Token(
    val accessToken: String,
    val refreshToken: String = "",
)
