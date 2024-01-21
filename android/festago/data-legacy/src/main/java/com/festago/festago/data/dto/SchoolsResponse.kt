package com.festago.festago.data.dto

import com.festago.festago.model.School
import kotlinx.serialization.Serializable

@Serializable
data class SchoolsResponse(
    val schools: List<SchoolResponse>
) {
    fun toDomain(): List<School> = schools.map { it.toDomain() }
}
