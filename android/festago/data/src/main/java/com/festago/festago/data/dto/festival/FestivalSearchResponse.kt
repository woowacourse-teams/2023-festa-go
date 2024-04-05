package com.festago.festago.data.dto.festival

import com.festago.festago.data.dto.artist.ArtistResponse
import com.festago.festago.domain.model.search.FestivalSearch
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class FestivalSearchResponse(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val posterImageUrl: String,
    val artists: List<ArtistResponse>,
) {
    fun toDomain(): FestivalSearch = FestivalSearch(
        id = id,
        name = name,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        imageUrl = posterImageUrl,
        artists = artists.map { it.toDomain() },
    )
}
