package com.festago.festago.data.dto.festival

import com.festago.festago.data.dto.artist.ArtistResponse
import com.festago.festago.data.dto.school.SchoolResponse

data class FestivalResponse(
    val artists: List<ArtistResponse>,
    val endDate: String,
    val id: Int,
    val imageUrl: String,
    val name: String,
    val school: SchoolResponse,
    val startDate: String,
)
