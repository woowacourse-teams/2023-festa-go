package com.festago.festago.domain.model.token

import java.time.LocalDateTime

data class Token(
    val token: String,
    val expiredAt: LocalDateTime,
) {
    fun isExpired() = LocalDateTime.now().isAfter(expiredAt)
}
