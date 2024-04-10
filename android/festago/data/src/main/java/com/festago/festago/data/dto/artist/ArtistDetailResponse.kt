package com.festago.festago.data.dto.artist

import com.festago.festago.data.dto.school.SocialMediaResponse
import com.festago.festago.domain.model.artist.ArtistDetail
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDetailResponse(
    val id: Int,
    val name: String?,
    val profileImageUrl: String?,
    val backgroundImageUrl: String?,
    val socialMedias: List<SocialMediaResponse>,
) {
    fun toDomain() = ArtistDetail(
        id = id,
        artistName = name ?: "",
        profileUrl = profileImageUrl ?: "",
        backgroundUrl = backgroundImageUrl ?: "",
        artistMedia = socialMedias.map { it.toDomain() },
    )
}
