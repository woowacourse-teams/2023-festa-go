package com.festago.festago.data.dto.festival

import com.festago.festago.domain.model.festival.FestivalsPage
import kotlinx.serialization.Serializable

@Serializable
data class FestivalsResponse(
    val last: Boolean,
    val content: List<FestivalResponse>,
) {
    fun toDomain() = FestivalsPage(
        isLastPage = last,
        festivals = content.map { it.toDomain() },
    )
}
