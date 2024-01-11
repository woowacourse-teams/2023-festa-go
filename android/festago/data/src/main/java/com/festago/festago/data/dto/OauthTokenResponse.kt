package com.festago.festago.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OauthTokenResponse(
    val accessToken: String,
    val nickname: String,
    val isNew: Boolean,
)
