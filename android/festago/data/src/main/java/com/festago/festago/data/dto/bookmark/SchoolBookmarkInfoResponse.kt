package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.SchoolBookmarkInfo
import kotlinx.serialization.Serializable

@Serializable
data class SchoolBookmarkInfoResponse(
    val id: Long,
    val name: String,
    val logoUrl: String,
) {
    fun toDomain() = SchoolBookmarkInfo(
        id = id,
        name = name,
        logoUrl = logoUrl,
    )
}
