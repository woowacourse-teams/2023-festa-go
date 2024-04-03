package com.festago.festago.domain.model.search

import com.festago.festago.domain.model.artist.Artist
import java.time.LocalDate

data class FestivalSearch(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val artists: List<Artist>,
)
