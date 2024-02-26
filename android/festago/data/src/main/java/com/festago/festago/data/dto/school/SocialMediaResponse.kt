package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.social.SocialMedia
import kotlinx.serialization.Serializable

@Serializable
data class SocialMediaResponse(
    val type: String,
    val name: String,
    val logoUrl: String,
    val url: String,
) {
    fun toDomain(): SocialMedia = SocialMedia(
        type = type,
        name = name,
        logoUrl = logoUrl,
        url = url,
    )
}
