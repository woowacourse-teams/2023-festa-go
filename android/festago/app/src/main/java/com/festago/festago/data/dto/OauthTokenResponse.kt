package com.festago.festago.data.dto

import com.festago.festago.domain.model.Token
import kotlinx.serialization.Serializable

@Serializable
data class OauthTokenResponse(
    val accessToken: String,
    val nickname: String,
) {
    fun toToken(): Token = Token(
        accessToken = accessToken,
    )
}
