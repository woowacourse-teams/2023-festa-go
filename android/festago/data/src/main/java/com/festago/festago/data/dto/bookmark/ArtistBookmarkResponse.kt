package com.festago.festago.data.dto.bookmark

import java.time.LocalDateTime

data class ArtistBookmarkResponse(
    val artist: ArtistBookmarkInfoResponse,
    val createdAt: LocalDateTime
) 
