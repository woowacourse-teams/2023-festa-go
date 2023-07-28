package com.festago.festago.presentation.model

import java.time.LocalDate

data class FestivalUiModel(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thumbnail: String,
)
