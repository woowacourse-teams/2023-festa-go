package com.festago.festago.data.dto.schooldetail

import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.school.School
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SchoolFestivalResponse(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String,
    val artists: List<SchoolFestivalArtistResponse>
) {
    fun toDomain() = Festival(
        id = id.toLong(),
        name = name,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        imageUrl = imageUrl,
        school = School(
            id = -1,
            name = "",
            imageUrl = ""
        ),
        artists = artists.map { it.toDomain() }
    )
}
