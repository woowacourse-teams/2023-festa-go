package com.festago.festago.data.model

import com.festago.festago.domain.model.token.Token
import java.time.LocalDateTime

data class TokenEntity(
    val token: String,
    val expiredAt: String,
) {
    fun toDomain() = Token(
        token = token,
        expiredAt = LocalDateTime.parse(expiredAt),
    )
}
