package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.school.School
import kotlinx.serialization.Serializable

@Serializable
data class SchoolResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String = "",
) {
    fun toDomain() = School(
        id = id,
        name = name,
        imageUrl = profileImageUrl,
    )
}
