package com.festago.festago.data.dto

import com.festago.festago.model.School
import kotlinx.serialization.Serializable

@Serializable
data class SchoolResponse(
    val domain: String,
    val id: Int,
    val name: String
) {
    fun toDomain(): School = School(
        id = id.toLong(),
        domain = domain,
        name = name
    )
}
