package com.festago.festago.data.dto

import com.festago.festago.domain.model.MemberTicketFestival
import kotlinx.serialization.Serializable

@Serializable
data class MemberTicketFestivalResponse(
    val id: Int,
    val name: String,
    val thumbnail: String,
) {
    fun toDomain(): MemberTicketFestival = MemberTicketFestival(
        id = id,
        name = name,
        thumbnail = thumbnail,
    )
}
