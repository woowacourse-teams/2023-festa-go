package com.festago.festago.data.dto

import com.festago.festago.model.School
import kotlinx.serialization.Serializable

@Serializable
data class SchoolResponse(
    val id: Int,
    val domain: String,
    val name: String
) {
    fun toDomain(): School = School(
        id = id.toLong(),
        domain = domain,
        name = name
    )
}
