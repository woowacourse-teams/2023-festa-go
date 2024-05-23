package com.festago.festago.domain.model.social

data class SocialMedia(
    val type: SocialMediaType,
    val name: String,
    val logoUrl: String,
    val url: String
)
