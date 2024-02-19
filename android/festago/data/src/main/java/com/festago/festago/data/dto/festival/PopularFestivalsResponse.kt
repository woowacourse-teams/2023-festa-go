package com.festago.festago.data.dto.festival

import com.festago.festago.domain.model.festival.PopularFestivals
import kotlinx.serialization.Serializable

@Serializable
data class PopularFestivalsResponse(
    val title: String,
    val content: List<FestivalResponse>,
) {
    fun toDomain() = PopularFestivals(
        title = title,
        festivals = content.map { it.toDomain() },
    )
}
