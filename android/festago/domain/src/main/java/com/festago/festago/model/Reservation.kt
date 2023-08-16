package com.festago.festago.model

import java.time.LocalDate

data class Reservation(
    val id: Int,
    val name: String,
    val reservationStages: List<ReservationStage>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thumbnail: String,
)
