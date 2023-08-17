package com.festago.festago.data.dto

import com.festago.festago.model.MemberTicketFestival
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
