package com.festago.festago.data.dto.festival

import com.festago.festago.data.dto.artist.ArtistResponse
import com.festago.festago.data.dto.school.SchoolResponse
import com.festago.festago.domain.model.festival.Festival
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class FestivalResponse(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String,
    val school: SchoolResponse,
    val artists: List<ArtistResponse>,
) {
    fun toDomain(): Festival = Festival(
        id = id,
        name = name,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        imageUrl = imageUrl,
        school = school.toDomain(),
        artists = artists.map { it.toDomain() },
    )
}
