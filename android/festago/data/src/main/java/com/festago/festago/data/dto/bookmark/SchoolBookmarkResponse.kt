package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.SchoolBookmark
import java.time.LocalDateTime

data class SchoolBookmarkResponse(
    val school: SchoolBookmarkInfoResponse,
    val createdAt: LocalDateTime,
) {
    fun toDomain() = SchoolBookmark(
        school = school.toDomain(),
        createdAt = createdAt,
    )
}
