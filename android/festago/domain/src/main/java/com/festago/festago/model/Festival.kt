package com.festago.festago.model

import java.time.LocalDate

data class Festival(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thumbnail: String,
)
