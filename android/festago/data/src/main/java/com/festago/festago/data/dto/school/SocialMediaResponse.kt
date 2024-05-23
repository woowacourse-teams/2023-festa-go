package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.model.social.SocialMediaType
import kotlinx.serialization.Serializable

@Serializable
data class SocialMediaResponse(
    val type: String,
    val name: String,
    val logoUrl: String,
    val url: String,
) {
    fun toDomain(): SocialMedia {
        val type = when (this.type) {
            "FACEBOOK" -> SocialMediaType.FACEBOOK
            "INSTAGRAM" -> SocialMediaType.INSTAGRAM
            "YOUTUBE" -> SocialMediaType.YOUTUBE
            "X" -> SocialMediaType.X
            else -> SocialMediaType.NONE
        }

        return SocialMedia(type = type, name = name, logoUrl = logoUrl, url = url)
    }
}
