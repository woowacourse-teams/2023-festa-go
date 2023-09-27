package com.festago.festago.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OauthRequestLegacy(
    val socialType: String,
    val accessToken: String,
)
