package com.festago.festago.data.dto

import com.festago.festago.domain.model.TicketCode
import kotlinx.serialization.Serializable

@Serializable
data class TicketCodeDto(
    val code: String,
    val period: Int,
) {
    fun toDomain(): TicketCode {
        return TicketCode(
            code = code,
            period = period,
        )
    }
}
