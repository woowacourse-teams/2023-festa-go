package com.festago.festago.data.dto.schooldetail

import com.festago.festago.domain.model.artist.Artist
import kotlinx.serialization.Serializable

@Serializable
data class SchoolFestivalArtistResponse(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
) {
    fun toDomain() = Artist(
        id = id.toLong(),
        name = name,
        imageUrl = profileImageUrl,
    )
}
