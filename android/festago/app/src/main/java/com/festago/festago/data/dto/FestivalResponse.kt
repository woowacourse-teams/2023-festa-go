package com.festago.festago.data.dto

import com.festago.festago.domain.model.Festival
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class FestivalResponse(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String,
) {
    fun toDomain() = Festival(
        id = id.toLong(),
        name = name,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        thumbnail = thumbnail,
    )
}
