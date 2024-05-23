package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.SchoolBookmark
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SchoolBookmarkResponse(
    val school: SchoolBookmarkInfoResponse,
    val createdAt: String,
) {
    fun toDomain() = SchoolBookmark(
        school = school.toDomain(),
        createdAt = LocalDateTime.parse(createdAt),
    )
}
