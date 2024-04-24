package com.festago.festago.data.dto.user

import com.festago.festago.data.model.TokenEntity
import com.festago.festago.domain.model.token.Token
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TokenResponse(
    val token: String,
    val expiredAt: String,
) {
    fun toDomain() = Token(
        token = token,
        expiredAt = LocalDateTime.parse(expiredAt),
    )

    fun toEntity() = TokenEntity(
        token = token,
        expiredAt = expiredAt,
    )
}
