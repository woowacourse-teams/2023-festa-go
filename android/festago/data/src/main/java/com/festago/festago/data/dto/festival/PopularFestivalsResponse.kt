package com.festago.festago.data.dto.festival

import com.festago.festago.domain.model.festival.PopularFestivals
import kotlinx.serialization.Serializable

@Serializable
data class PopularFestivalsResponse(
    val title: String,
    val contents: List<FestivalResponse>,
) {
    fun toDomain() = PopularFestivals(
        title = title,
        festivals = contents.map { it.toDomain() },
    )
}
