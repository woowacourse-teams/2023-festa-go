package com.festago.festago.data.dto.bookmark

import java.time.LocalDateTime

data class SchoolBookmarkResponse(
    val school: SchoolBookmarkInfoResponse,
    val createdAt: LocalDateTime
)
