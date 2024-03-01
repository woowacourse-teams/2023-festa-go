package com.festago.festago.data.dto.schooldetail

import com.festago.festago.domain.model.festival.FestivalsPage
import kotlinx.serialization.Serializable

@Serializable
data class SchoolFestivalsResponse(
    val last: Boolean,
    val content: List<SchoolFestivalResponse>
) {
    fun toDomain() = FestivalsPage(
        isLastPage = last,
        festivals = content.map { it.toDomain() },
    )
}
