package com.festago.festago.domain.model.bookmark

import java.time.LocalDateTime

data class SchoolBookmark(
    val school: SchoolBookmarkInfo,
    val createdAt: LocalDateTime
)
