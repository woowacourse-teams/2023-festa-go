package com.festago.festago.data.dto

import com.festago.festago.model.Reservation
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ReservationFestivalResponse(
    val id: Int,
    val schoolId: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String,
    val stages: List<ReservationStageResponse>,
) {
    fun toDomain(): Reservation = Reservation(
        id = id,
        name = name,
        reservationStages = stages.map { it.toDomain() },
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        thumbnail = thumbnail,
    )
}
