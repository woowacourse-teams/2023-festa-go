package com.festago.festago.data.dto.artist

import com.festago.festago.domain.model.search.ArtistSearch
import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val todayStage: Int,
    val plannedStage: Int,
) {
    fun toDomain() = ArtistSearch(
        id = id,
        name = name,
        profileImageUrl = profileImageUrl,
        todayStage = todayStage,
        upcomingStage = plannedStage,
    )
}
