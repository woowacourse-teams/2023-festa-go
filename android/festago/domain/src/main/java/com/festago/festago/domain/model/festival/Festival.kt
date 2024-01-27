package com.festago.festago.domain.model.festival

import com.festago.festago.domain.model.artist.Artist
import com.festago.festago.domain.model.school.School
import java.time.LocalDate

data class Festival(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val school: School,
    val artists: List<Artist>,
)
