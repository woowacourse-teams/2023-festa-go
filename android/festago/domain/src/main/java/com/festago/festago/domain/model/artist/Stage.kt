package com.festago.festago.domain.model.artist

import java.time.LocalDate

data class Stage(
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val artists: List<Artist>,
)
