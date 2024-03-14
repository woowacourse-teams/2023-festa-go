package com.festago.festago.domain.model.search

import java.time.LocalDate

data class SchoolSearch(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val upcomingFestivalStartDate: LocalDate,
)
