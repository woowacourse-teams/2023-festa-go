package com.festago.festago.data.dto.artist

import com.festago.festago.domain.model.artist.Artist

data class ArtistResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
) {
    fun toDomain() = Artist(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )
}
