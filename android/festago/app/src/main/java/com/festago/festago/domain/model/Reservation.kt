package com.festago.festago.domain.model

import java.time.LocalDateTime

data class Reservation(
    val id: Int,
    val name: String,
    val reservationStages: List<ReservationStage>,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val thumbnail: String,
)
