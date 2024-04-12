package com.festago.festago.domain.model.bookmark

import java.time.LocalDateTime

data class ArtistBookmark(
    val artist: ArtistBookmarkInfo,
    val createdAt: LocalDateTime
) 
