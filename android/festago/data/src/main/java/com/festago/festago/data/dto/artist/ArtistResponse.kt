package com.festago.festago.data.dto.artist

import com.festago.festago.domain.model.artist.Artist
import kotlinx.serialization.Serializable

@Serializable
data class ArtistResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
) {
    fun toDomain() = Artist(
        id = id,
        name = name,
        imageUrl = profileImageUrl,
    )
}
