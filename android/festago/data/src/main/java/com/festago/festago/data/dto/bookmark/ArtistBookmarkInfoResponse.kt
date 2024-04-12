package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.ArtistBookmarkInfo
import kotlinx.serialization.Serializable

@Serializable
data class ArtistBookmarkInfoResponse(
    val name: String,
    val profileImageUrl: String
) {
    fun toDomain() = ArtistBookmarkInfo(
        name = name,
        profileImageUrl = profileImageUrl
    )
}
