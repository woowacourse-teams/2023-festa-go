package com.festago.festago.domain.model.artist

data class ArtistDetail(
    val id: Int,
    val artistName: String,
    val logoUrl: String,
    val backgroundUrl: String,
    val artistMedia: List<ArtistMedia>,
)
