package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.search.SchoolSearch
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SchoolSearchResponse(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val upcomingFestivalStartDate: String?,
) {
    fun toDomain() = SchoolSearch(
        id = id,
        name = name,
        logoUrl = logoUrl,
        upcomingFestivalStartDate = upcomingFestivalStartDate?.let { LocalDate.parse(it) },
    )
}
