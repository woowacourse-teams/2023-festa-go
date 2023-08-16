package com.festago.festago.presentation.model

import java.time.LocalDate

data class ReservationFestivalUiState(
    val id: Int,
    val name: String,
    val thumbnail: String,
    val endDate: LocalDate,
    val startDate: LocalDate,
)
