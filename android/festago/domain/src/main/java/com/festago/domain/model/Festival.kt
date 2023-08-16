package com.festago.festago.domain.model

import java.time.LocalDate

data class Festival(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thumbnail: String,
)
