package com.festago.festago.domain.model.artist

import com.festago.festago.domain.model.social.SocialMedia

data class ArtistDetail(
    val id: Int,
    val artistName: String,
    val profileUrl: String,
    val backgroundUrl: String,
    val artistMedia: List<SocialMedia>,
)
