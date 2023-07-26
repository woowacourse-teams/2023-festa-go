package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class ReservationUiModel(
    val id: Int,
    val name: String,
    val thumbnail: String,
    val endDate: LocalDateTime,
    val startDate: LocalDateTime,
    val reservationStages: List<ReservationStageUiModel>,
)
