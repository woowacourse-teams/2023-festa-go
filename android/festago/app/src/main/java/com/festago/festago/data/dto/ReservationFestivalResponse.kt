package com.festago.festago.data.dto

import com.festago.festago.domain.model.Reservation
import java.time.LocalDateTime

data class ReservationFestivalResponse(
    val id: Int,
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
        startDate = LocalDateTime.parse(startDate),
        endDate = LocalDateTime.parse(endDate),
        thumbnail = thumbnail,
    )
}
